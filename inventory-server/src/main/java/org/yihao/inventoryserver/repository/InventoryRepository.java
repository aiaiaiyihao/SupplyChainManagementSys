package org.yihao.inventoryserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yihao.inventoryserver.model.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
