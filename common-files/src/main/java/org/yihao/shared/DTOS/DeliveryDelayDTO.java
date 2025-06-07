package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDelayDTO {
    private Long deliveryId;
    private Long driverId;
    private String driverName;
    private String driveEmail;
    private LocalDateTime estimatedDeliveryTime;
}
