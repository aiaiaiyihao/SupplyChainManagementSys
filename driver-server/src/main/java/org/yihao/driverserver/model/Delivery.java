package org.yihao.driverserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.DeliveryStatus;
import org.yihao.shared.ENUMS.DeliveryType;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    // Reference to the order being delivered
    private Long orderId;

    // Reference to the driver performing the delivery
    private Long driverId;

    // Type of delivery: either picking up the ordered product or returning it
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    // Status of the delivery process
    @Enumerated(EnumType.STRING)
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