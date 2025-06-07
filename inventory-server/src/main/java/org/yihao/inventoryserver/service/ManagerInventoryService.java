package org.yihao.inventoryserver.service;

import org.yihao.shared.DTOS.InventoryDTO;
import org.yihao.shared.DTOS.InventoryMovementDTO;
import org.yihao.shared.DTOS.InventoryQueryDTO;
import org.yihao.shared.DTOS.InventoryResponse;

public interface ManagerInventoryService {
    InventoryResponse getAllInventoriesPage(Integer pageNumber, Integer pageSize);

    InventoryDTO getInventoryById(Long inventoryId);

    InventoryDTO createInventory(InventoryDTO inventoryDTO);

    InventoryDTO getInventoryByProductIdAndWarehouseId(InventoryQueryDTO queryDTO);

    InventoryDTO updateInventoryById(Long inventoryId, InventoryDTO inventoryDTO);

    InventoryDTO updateInventoryByMovement(InventoryMovementDTO inventoryMovementDTO);

}
