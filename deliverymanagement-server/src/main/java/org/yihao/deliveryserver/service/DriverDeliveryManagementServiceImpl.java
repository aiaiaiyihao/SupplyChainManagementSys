package org.yihao.deliveryserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.yihao.deliveryserver.service.Remote.RemoteDeliveryService;
import org.yihao.deliveryserver.service.Remote.RemoteInventoryService;
import org.yihao.deliveryserver.service.Remote.RemoteOrderService;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.DeliveryType;
import org.yihao.shared.ENUMS.MovementType;
import org.yihao.shared.ENUMS.OrderStatus;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class DriverDeliveryManagementServiceImpl implements DriverDeliveryManagementService {
    private final RemoteOrderService remoteOrderService;
    private final RemoteDeliveryService remoteDeliveryService;
    private final RemoteInventoryService remoteInventoryService;
    private final KafkaTemplate<String,DeliveryNotificationDTO> kafkaTemplate;
//    private KafkaTemplate<String,DeliveryNotificationDTO> kafkaTemplate;

    public DriverDeliveryManagementServiceImpl(
            RemoteDeliveryService remoteDeliveryService,
            RemoteOrderService remoteOrderService,
            RemoteInventoryService remoteInventoryService,
            KafkaTemplate<String,DeliveryNotificationDTO> kafkaTemplate
//            KafkaTemplate<String,DeliveryNotificationDTO> kafkaTemplate
    ) {
        this.remoteDeliveryService = remoteDeliveryService;
        this.remoteOrderService = remoteOrderService;
        this.remoteInventoryService = remoteInventoryService;
        this.kafkaTemplate = kafkaTemplate;
//        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public DeliveryDTO changeDriverStatus(String role, Long driverId, DeliveryStatusChangeRequest request) {
        DeliveryDTO deliveryDTOChanged = remoteDeliveryService.updateDeliveryStatus(role,driverId,request).getBody();
        OrderStatusUpdateRequest orderRequest = new OrderStatusUpdateRequest();
        orderRequest.setDeliveryId(deliveryDTOChanged.getDeliveryId());
        orderRequest.setOrderId(deliveryDTOChanged.getOrderId());
        DeliveryType deliveryType = deliveryDTOChanged.getDeliveryType();
        //骑手改为正在配送
        if (!request.getDeliveried()) {
            //update order status to Delivering
            orderRequest.setStatus(deliveryType.equals(DeliveryType.PICKUP)?
                    OrderStatus.DELIVERING:OrderStatus.RETURNDELIVERING);
            OrderDTO orderDTO = remoteOrderService.updateOrderStatus(role,orderRequest).getBody();
            //add inventory change if return delivery and delivering
            if(deliveryType.equals(DeliveryType.RETURN)){
                InventoryMovementDTO inventoryMovementDTO = buildInventoryMovement(MovementType.OUTBOUND, DeliveryType.RETURN, orderDTO);
                remoteInventoryService.updateInventoryByMovement(inventoryMovementDTO);
            }
        //骑手改为已送达
        } else {
            orderRequest.setStatus(deliveryType.equals(DeliveryType.PICKUP)?
                    OrderStatus.DELIVERED:OrderStatus.RETURNDELIVERED);
            OrderDTO orderDTO = remoteOrderService.updateOrderStatus(role,orderRequest).getBody();
            //add inventory change if pickup delivery and delivered
            if (deliveryDTOChanged.getDeliveryType().equals(DeliveryType.PICKUP)){
                InventoryMovementDTO inventoryMovementDTO
                        = buildInventoryMovement(MovementType.INBOUND, DeliveryType.PICKUP, orderDTO);
                remoteInventoryService.updateInventoryByMovement(inventoryMovementDTO);
            }
            //update driver status
            // if driver still have task don't change, no task left change to IDLE
            DriverDTO driverDTO = remoteDeliveryService.driverStatusRefresh(role, driverId).getBody();
            if(driverDTO==null){
                throw new RuntimeException("Driver status refresh failed");
            }


            //配送超时
            if(deliveryDTOChanged.getEstimatedDeliveryTime().isBefore(LocalDateTime.now())){
                DeliveryNotificationDTO deliveryNotificationDTO = new DeliveryNotificationDTO();
                deliveryNotificationDTO.setDeliveryDTO(deliveryDTOChanged);
                deliveryNotificationDTO.setDriverEmail(driverDTO.getEmail());
                deliveryNotificationDTO.setDriverName(driverDTO.getFirstName()+" "+driverDTO.getLastName());
                generateEmail(AppConstants.TOPIC_NAME_DELIVERY_DELAY_DRIVER,deliveryNotificationDTO);
            }
        }
        return deliveryDTOChanged;
    }
    private InventoryMovementDTO buildInventoryMovement(MovementType type, DeliveryType deliveryType, OrderDTO orderDTO) {
        InventoryMovementDTO dto = new InventoryMovementDTO();
        dto.setMovementType(type);
        if(deliveryType.equals(DeliveryType.PICKUP)){
            dto.setReason("purchasing order inbound");
        } else{
            dto.setReason("returning order outbound");
        }
        dto.setQuantity(orderDTO.getProductQuantity());
        dto.setProductId(orderDTO.getProductId());
        dto.setProductName(orderDTO.getProductName());
        dto.setWarehouseId(orderDTO.getWarehouseId());
        return dto;
    }


    private void generateEmail(String topic,DeliveryNotificationDTO deliveryNotificationDTO) {
        CompletableFuture<SendResult<String, DeliveryNotificationDTO>> future
                = kafkaTemplate.send(topic, deliveryNotificationDTO);
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
