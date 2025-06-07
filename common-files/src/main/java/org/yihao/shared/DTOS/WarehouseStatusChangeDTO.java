package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.WarehouseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStatusChangeDTO {
    private Long warehouseId;
    private WarehouseStatus status;
}
