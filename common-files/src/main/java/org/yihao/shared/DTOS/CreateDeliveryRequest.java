package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryRequest {
    Long orderId;
    Long warehouseId;
    String instruction;
}
