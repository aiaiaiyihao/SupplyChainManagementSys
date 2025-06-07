package org.yihao.orderserver.Specification;

import org.springframework.data.jpa.domain.Specification;
import org.yihao.orderserver.Model.Order;
import org.yihao.shared.ENUMS.OrderStatus;

public class OrderSpecification {

    public static Specification<Order> hasProductId(Long productId) {
        return (root, query, cb)
                -> productId == null ? null : cb.equal(root.get("productId"), productId);
    }
    public static Specification<Order> hasProductNameLike(String productName) {
        return (root, query, cb)
                -> productName == null ? null : cb.like(cb.lower(root.get("productName")), "%" + productName.toLowerCase() + "%");
    }

    public static Specification<Order> hasSupplierId(Long supplierId) {
        return (root, query, cb)
                -> supplierId == null ? null : cb.equal(root.get("supplierId"), supplierId);
    }

    public static Specification<Order> hasSupplierNameLike(String supplierName) {
        return (root, query, cb)
                -> supplierName == null ? null : cb.like(cb.lower(root.get("supplierName")), "%" + supplierName.toLowerCase() + "%");
    }

    public static Specification<Order> hasOrderStatus(OrderStatus status) {
        return (root, query, cb)
                -> status == null ? null : cb.equal(root.get("orderStatus"), status);
    }

    public static Specification<Order> hasDeliveryId(Long deliveryId) {
        return (root, query, cb)
                -> deliveryId == null ? null : cb.equal(root.get("deliveryId"), deliveryId);
    }

    public static Specification<Order> hasFactoryId(Long factoryId) {
        return (root, query, cb)
                -> factoryId == null ? null : cb.equal(root.get("factoryId"), factoryId);
    }

    public static Specification<Order> hasWarehouseId(Long warehouseId) {
        return (root, query, cb)
                -> warehouseId == null ? null : cb.equal(root.get("warehouseId"), warehouseId);
    }

}
