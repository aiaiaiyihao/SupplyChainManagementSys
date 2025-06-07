package org.yihao.deliveryserver.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.yihao.deliveryserver.Exception.APIException;
import org.yihao.deliveryserver.service.Remote.RemoteDeliveryService;
import org.yihao.deliveryserver.service.Remote.RemoteFacilityService;
import org.yihao.deliveryserver.service.Remote.RemoteOrderService;
import org.yihao.deliveryserver.service.Remote.RemoteSupplierService;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.DeliveryType;
import org.yihao.shared.ENUMS.DriverStatus;
import org.yihao.shared.ENUMS.OrderStatus;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class DeliverymanagementServiceImpl implements DeliverymanagementService {
    private final RemoteDeliveryService remoteDeliveryService;
    private final RemoteOrderService remoteOrderService;
    private final RemoteSupplierService remoteSupplierService;
    private final RemoteFacilityService remoteFacilityService;
    private final KafkaTemplate<String, DeliveryNotificationDTO> kafkaTemplate;

    public DeliverymanagementServiceImpl(RemoteDeliveryService remoteDeliveryService,
                                         RemoteOrderService remoteOrderService,
                                         RemoteSupplierService remoteSupplierService,
                                         RemoteFacilityService remoteFacilityService,
                                         KafkaTemplate<String,DeliveryNotificationDTO> kafkaTemplate) {
        this.remoteDeliveryService = remoteDeliveryService;
        this.remoteOrderService = remoteOrderService;
        this.remoteSupplierService = remoteSupplierService;
        this.remoteFacilityService = remoteFacilityService;
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public DeliveryDTO addDelivery(CreateDeliveryRequest request) {
        OrderDTO orderDTO = Optional.ofNullable(remoteOrderService.getOrderById(Role.MANAGER.name(),request.getOrderId()).getBody())
                .orElseThrow(() -> new APIException("Order not found."));
        //I can't generate delivery unless it approved.
        OrderStatus orderStatus = orderDTO.getOrderStatus();
        if(orderDTO.getOrderStatus()!= OrderStatus.APPROVED &&
        orderDTO.getOrderStatus()!=OrderStatus.RETURNAPPROVED) {
            throw new APIException("The order status is: "+orderStatus.name()
                    +". The delivery cannot be generated unless the status is approved.");
        }
        //find available drivers and choose one of them
        DriverDTO randomDriver = remoteDeliveryService.getAvailableDriver(Role.MANAGER.name()).getBody();
        if (randomDriver == null) {
            throw new APIException("No available drivers at the moment.");
        }

        DeliveryDTO deliveryDTO = new DeliveryDTO();
        if(orderDTO.getOrderStatus()== OrderStatus.APPROVED) {
            deliveryDTO.setDeliveryType(DeliveryType.PICKUP);
        } else{
            deliveryDTO.setDeliveryType(DeliveryType.RETURN);
        }

        deliveryDTO.setOrderId(orderDTO.getOrderId());
        deliveryDTO.setDriverId(randomDriver.getDriverId());
        deliveryDTO.setFactoryId(orderDTO.getFactoryId());

        deliveryDTO.setInstructions(request.getInstruction());
        Long warehouseId;
        if(request.getWarehouseId()!=null) {
            warehouseId = request.getWarehouseId();
            deliveryDTO.setWarehouseId(warehouseId);
        } else if(orderDTO.getWarehouseId()!=null) {
            warehouseId = orderDTO.getWarehouseId();
            deliveryDTO.setWarehouseId(warehouseId);
        } else {
            //need to find the common warehouse
            throw new APIException("Warehouse id is not assigned.");
        }
        OrderDTO updatedWarehouseId = new OrderDTO();
        updatedWarehouseId.setWarehouseId(warehouseId);
        remoteOrderService.updateOrder(Role.MANAGER.name(), orderDTO.getOrderId(),updatedWarehouseId);

        DeliveryDTO deliveryGenerated = remoteDeliveryService.addDelivery(Role.MANAGER.name(), deliveryDTO).getBody();
        if(deliveryGenerated==null) {
            throw new APIException("Delivery generation failed.");
        }
        Address factoryAddress = remoteSupplierService
                .findAddressByFactoryId(Role.MANAGER.name(), orderDTO.getSupplierId(), deliveryGenerated.getFactoryId()).getBody();

        OrderStatusUpdateRequest orderRequest = new OrderStatusUpdateRequest();
        orderRequest.setOrderId(deliveryGenerated.getOrderId());
        orderRequest.setDeliveryId(deliveryGenerated.getDeliveryId());
        if(orderDTO.getOrderStatus()== OrderStatus.APPROVED) {
            orderRequest.setStatus(OrderStatus.APPOINTED);
        } else{
            orderRequest.setStatus(OrderStatus.RETURNAPPOINTED);
        }

        OrderDTO order= remoteOrderService.updateOrderStatus(Role.MANAGER.name(), orderRequest).getBody();
        if (order == null) {
            throw new APIException("Order update failed.");
        }
        DriverStatusChangeRequest statusChangeRequest = new DriverStatusChangeRequest();
        statusChangeRequest.setDriverId(randomDriver.getDriverId());
        statusChangeRequest.setStatus(DriverStatus.APPOINTED);
        DriverDTO driverDTO = remoteDeliveryService.updateDriverStatus(Role.MANAGER.name(), statusChangeRequest).getBody();
        if(driverDTO==null) {
            throw new APIException("Driver update failed.");
        }

        WarehouseDTO warehouseDTO = remoteFacilityService
                .getWarehouseById(Role.MANAGER.name(),deliveryGenerated.getWarehouseId()).getBody();
        if(warehouseDTO==null) {
            throw new APIException("Warehouse update failed.");
        }

//        OrderDTO order = remoteOrderService.getOrderById(deliveryDTO.getOrderId()).getBody();

        SupplierDTO supplierDTO = remoteSupplierService.getSupplierById(Role.MANAGER.name(),order.getSupplierId()).getBody();
        if(supplierDTO==null) {
            throw new APIException("Supplier update failed.");
        }

        //notification
        DeliveryNotificationDTO deliveryNotificationDTO = new DeliveryNotificationDTO();
        deliveryNotificationDTO.setDeliveryDTO(deliveryGenerated);
        deliveryNotificationDTO.setPickupAddress(factoryAddress);
        deliveryNotificationDTO.setInventoryAddress(warehouseDTO.getAddress());
        String supplierName = order.getSupplierName();
        deliveryNotificationDTO.setSupplierName(supplierName);

        deliveryNotificationDTO.setSupplierEmail(supplierDTO.getEmail());
        deliveryNotificationDTO.setDriverName(randomDriver.getFirstName()+" "+randomDriver.getLastName());
        deliveryNotificationDTO.setDriverNumber(randomDriver.getPhoneNumber());
        deliveryNotificationDTO.setDriverEmail(randomDriver.getEmail());
        deliveryNotificationDTO.setProductName(order.getProductName());
        deliveryNotificationDTO.setProductQuantity(order.getProductQuantity());
        generateEmail(deliveryNotificationDTO);
        return deliveryGenerated;
    }

    private void generateEmail(DeliveryNotificationDTO deliveryNotificationDTO) {
        CompletableFuture<SendResult<String, DeliveryNotificationDTO>> future
                = kafkaTemplate.send(AppConstants.TOPIC_NAME_DELIVERY, deliveryNotificationDTO);
        future.whenComplete((res, err) -> {
            if (Objects.isNull(err)) {
                log.info("send message=[{}] with offset=[{}]",
                        deliveryNotificationDTO.toString(), res.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message=[{}] due to: {}",
                        deliveryNotificationDTO.toString(), err.getMessage());
            }
        });
    }
}
