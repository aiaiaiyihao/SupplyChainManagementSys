package org.yihao.inventoryserver.service;

import org.yihao.shared.DTOS.InventoryMovementDTO;
import org.yihao.shared.DTOS.InventoryMovementResponse;
import org.yihao.shared.ENUMS.MovementType;

public interface ManagerInOutBoundService {
    InventoryMovementResponse getAllInoutBound(MovementType movementType, Long inventoryId, Long productId, String productName, Long warehouseId, Boolean createdToday, String sortBy, boolean desc, Integer pageNumber, Integer pageSize);

    InventoryMovementDTO updateInOutBoundById(Long inventoryMovementId, InventoryMovementDTO inventoryMovementDTO);
}
