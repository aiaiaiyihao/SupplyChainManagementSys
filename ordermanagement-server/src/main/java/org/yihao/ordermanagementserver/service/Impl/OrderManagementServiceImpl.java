package org.yihao.ordermanagementserver.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yihao.ordermanagementserver.DTO.*;
import org.yihao.ordermanagementserver.Enum.ProductPhase;
import org.yihao.ordermanagementserver.Exception.APIException;
import org.yihao.ordermanagementserver.service.OrderManagementService;
import org.yihao.ordermanagementserver.service.remote.RemoteOrderService;
import org.yihao.ordermanagementserver.service.remote.RemoteProductService;
import org.yihao.ordermanagementserver.service.remote.RemoteSupplierService;
import org.yihao.shared.DTOS.CreateOrderRequest;
import org.yihao.shared.DTOS.OrderApprovalDTO;
import org.yihao.shared.DTOS.OrderDTO;
import org.yihao.shared.DTOS.SupplierDTO;
import org.yihao.shared.config.AppConstants;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderManagementServiceImpl implements OrderManagementService {
    private RemoteProductService remoteProductService;
    private RemoteOrderService remoteOrderService;
    private RemoteSupplierService remoteSupplierService;
    private KafkaTemplate<String,OrderDTO> kafkaTemplate;

    public OrderManagementServiceImpl(
            RemoteProductService remoteProductService, RemoteOrderService remoteOrderService,
          RemoteSupplierService remoteSupplierService, KafkaTemplate<String,OrderDTO> kafkaTemplate
    ) {
        this.remoteProductService = remoteProductService;
        this.remoteOrderService = remoteOrderService;
        this.remoteSupplierService = remoteSupplierService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public OrderDTO createOrder(String role, CreateOrderRequest createOrderRequest) {
        Long productId = createOrderRequest.getProductId();
        Integer quantity = createOrderRequest.getQuantity();
        ProductDTO product = remoteProductService.getProduct(role,productId).getBody();
        if(product == null) {
            throw new APIException("PRODUCT NOT FOUND");
        }
        if(!product.getProductPhase().equals(ProductPhase.PRODUCTION)){
            throw new APIException("PRODUCT PHASE NOT IN PRODUCTION, CANNOT CREATE ORDER");
        }
        if(product.getWarehouseId()==null){
            throw new APIException("PRODUCT Not set Warehouse ID to store, please set Warehouse first");
        }
        SupplierDTO supplier = remoteSupplierService.getSupplierById(product.getSupplierId()).getBody();
        if(supplier == null) {
            throw new APIException("SUPPLIER NOT FOUND");
        }
        OrderDTO orderDTO = new OrderDTO();
        if(product.getProductId()!=null){
            orderDTO.setProductId(product.getProductId());
        }
        if(product.getWarehouseId()!=null){
            orderDTO.setWarehouseId(product.getWarehouseId());
        }
        if(product.getFactoryId()!=null){
            orderDTO.setFactoryId(product.getFactoryId());
        }
        orderDTO.setProductId(productId);
        orderDTO.setProductName(product.getProductName());
        orderDTO.setSupplierId(product.getSupplierId());
        orderDTO.setSupplierName(supplier.getSupplierName());

        orderDTO.setPerPrice(product.getProductPrice());
        orderDTO.setCurrency(product.getCurrency());
        orderDTO.setProductQuantity(quantity);
        OrderDTO orderCreated = remoteOrderService.createOrder(role,orderDTO).getBody();
        if(orderCreated == null) {
            throw new APIException("ORDER CREATED FAIL");
        }
        orderCreated.setEmail(supplier.getEmail());
        //send orderDTO to kafka
        CompletableFuture<SendResult<String, OrderDTO>> future
                = kafkaTemplate.send(AppConstants.TOPIC_NAME_ORDER, orderCreated);
        future.whenComplete((res, err) -> {
            if (Objects.isNull(err)) {
                log.info("send message=[{}] with offset=[{}]",
                        orderCreated.toString(), res.getRecordMetadata().offset());
            } else{
                log.error("Unable to send message=[{}] due to: {}",
                        orderCreated.toString(), err.getMessage());
            }
        });
        return orderCreated;
    }

    @Override
    public OrderDTO approveOrder(String role, Long tableId, OrderApprovalDTO orderApprovalDTO) {
        OrderDTO orderDTO = remoteOrderService.approveOrder(role,tableId,orderApprovalDTO).getBody();
        if(orderDTO==null){
            throw new APIException("order not approved");
        }
        String message = orderApprovalDTO.getMessage();
        if(message!=null){
            orderDTO.setMessage(message);
        }
        //if approved, select rider and generate delivery
        if(orderApprovalDTO.getApprovalResult()){
            sendEmail(orderDTO,AppConstants.TOPIC_NAME_ORDERAPPROVAL);
        } else{
            sendEmail(orderDTO,AppConstants.TOPIC_NAME_ORDERDENIAL);
        }
        return orderDTO;
    }

    @Override
    public OrderDTO requestReturnOrder(String role, Long orderId) {
        OrderDTO orderDTO = remoteOrderService.requestReturnOrder(role,orderId).getBody();
        if(orderDTO==null){
            throw new APIException("order not requested return successfully");
        }
        SupplierDTO supplier = remoteSupplierService.getSupplierById(orderDTO.getSupplierId()).getBody();
        if(supplier==null){
            throw new APIException("supplier not found");
        }
        orderDTO.setEmail(supplier.getEmail());
        sendEmail(orderDTO,AppConstants.TOPIC_NAME_ORDER);
        return orderDTO;
    }

    private void sendEmail(OrderDTO orderDTO,String topic) {
        CompletableFuture<SendResult<String, OrderDTO>> future
                = kafkaTemplate.send(topic, orderDTO);
        future.whenComplete((res, err) -> {
            if (Objects.isNull(err)) {
                log.info("send message=[{}] with offset=[{}]",
                        orderDTO.toString(), res.getRecordMetadata().offset());
            } else{
                log.error("Unable to send message=[{}] due to: {}",
                        orderDTO.toString(), err.getMessage());
            }
        });
    }
}
