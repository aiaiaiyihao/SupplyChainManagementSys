package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.DeliveryStatus;
import org.yihao.shared.ENUMS.DeliveryType;


import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long deliveryId;

    // Reference to the order being delivered
    private Long orderId;

    // Reference to the driver performing the delivery
    private Long driverId;

    // Type of delivery: either picking up the ordered product or returning it
    private DeliveryType deliveryType;

    // Status of the delivery process
    private DeliveryStatus deliveryStatus;

    // Timestamps for scheduling, pickup, and delivery events
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime estimatedDeliveryTime;

    // Addresses (as text, or you could use an Address entity if needed)
    private Long warehouseId;
    private Long factoryId;

    // Additional instructions or notes regarding the delivery
    private String instructions;
}