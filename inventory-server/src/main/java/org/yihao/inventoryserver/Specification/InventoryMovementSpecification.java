package org.yihao.inventoryserver.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.yihao.inventoryserver.model.Inventory;
import org.yihao.inventoryserver.model.InventoryMovement;
import org.yihao.shared.ENUMS.MovementType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class InventoryMovementSpecification {

    public static Specification<InventoryMovement> hasInventoryMovementType(MovementType movementType) {
        return (root, query, cb)
                -> movementType == null ? null : cb.equal(root.get("movementType"), movementType);
    }

    public static Specification<InventoryMovement> hasInventoryId(Long inventoryId) {
        return (Root<InventoryMovement> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (inventoryId == null) {
                return null; // No filter applied
            }
                /*Warehouse is your main entity.
                Address is a related entity, likely connected with a @OneToOne or @ManyToOne relationship.*/
            Join<InventoryMovement, Inventory> inventoryJoin
                        /*“In the Warehouse entity, join with the address field,
                        so I can access fields from the related Address entity.”*/
                    = root.join("inventory");
            return cb.equal(inventoryJoin.get("inventoryId"), inventoryId);
        };
    }

    public static Specification<InventoryMovement> hasProductId(Long productId) {
        return (Root<InventoryMovement> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (productId == null) {
                return null; // No filter applied
            }
                /*Warehouse is your main entity.
                Address is a related entity, likely connected with a @OneToOne or @ManyToOne relationship.*/
            Join<InventoryMovement, Inventory> inventoryJoin
                        /*“In the Warehouse entity, join with the address field,
                        so I can access fields from the related Address entity.”*/
                    = root.join("inventory");
            return cb.equal(inventoryJoin.get("productId"), productId);
        };
    }

    public static Specification<InventoryMovement> hasProductName(String productName) {
        return (Root<InventoryMovement> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (productName == null|| productName.trim().isEmpty()) {
                return null; // No filter applied
            }
                /*Warehouse is your main entity.
                Address is a related entity, likely connected with a @OneToOne or @ManyToOne relationship.*/
            Join<InventoryMovement, Inventory> inventoryJoin
                        /*“In the Warehouse entity, join with the address field,
                        so I can access fields from the related Address entity.”*/
                    = root.join("inventory");
            return cb.equal(inventoryJoin.get("productName"), productName);
        };
    }

    public static Specification<InventoryMovement> hasWarehouseId(Long warehouseId) {
        return (Root<InventoryMovement> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (warehouseId == null) {
                return null; // No filter applied
            }
                /*Warehouse is your main entity.
                Address is a related entity, likely connected with a @OneToOne or @ManyToOne relationship.*/
            Join<InventoryMovement, Inventory> inventoryJoin
                        /*“In the Warehouse entity, join with the address field,
                        so I can access fields from the related Address entity.”*/
                    = root.join("inventory");
            return cb.equal(inventoryJoin.get("warehouseId"), warehouseId);
        };
    }

    public static Specification<InventoryMovement> isCreatedToday() {
        return (Root<InventoryMovement> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            return cb.between(root.get("createdAt"), startOfDay, endOfDay);
        };
    }

}
