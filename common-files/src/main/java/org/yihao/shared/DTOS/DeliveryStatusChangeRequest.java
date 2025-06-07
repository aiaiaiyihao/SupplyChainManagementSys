package org.yihao.shared.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusChangeRequest {
    @NotNull(message = "deliveryId must not be null")
    private Long deliveryId;
    @NotNull(message = "deliveryStatus change must not be null")
    //false:change to deliverying  true:change to deliveried
    private Boolean deliveried;
}

