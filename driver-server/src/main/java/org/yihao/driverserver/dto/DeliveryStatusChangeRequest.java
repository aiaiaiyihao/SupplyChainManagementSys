package org.yihao.driverserver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusChangeRequest {
    @NotNull(message = "driverId must not be null")
    private Long driverId;
    @NotNull(message = "deliveryId must not be null")
    private Long deliveryId;
    @NotNull(message = "deliveryStatus change must not be null")
    private Boolean deliveried;
}

