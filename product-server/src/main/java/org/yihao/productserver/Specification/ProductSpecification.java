package org.yihao.productserver.Specification;

import org.springframework.data.jpa.domain.Specification;
import org.yihao.productserver.model.Product;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;

public class ProductSpecification {

    public static Specification<Product> hasProductId(Long productId) {
        return (root, query, cb)
                -> productId == null ? null : cb.equal(root.get("productId"), productId);
    }
    public static Specification<Product> hasProductNameLike(String productName) {
        return (root, query, cb)
                -> productName == null ? null : cb.like(cb.lower(root.get("productName")), "%" + productName.toLowerCase() + "%");
    }

    public static Specification<Product> hasSupplierId(Long supplierId) {
        return (root, query, cb)
                -> supplierId == null ? null : cb.equal(root.get("supplierId"), supplierId);
    }


    public static Specification<Product> hasProductPhase(ProductPhase productPhase) {
        return (root, query, cb)
                -> productPhase == null ? null : cb.equal(root.get("productPhase"), productPhase);
    }

    public static Specification<Product> hasProductCategory(ProductCategory productCategory) {
        return (root, query, cb)
                -> productCategory == null ? null : cb.equal(root.get("productCategory"), productCategory);
    }

    public static Specification<Product> hasFactoryId(Long factoryId) {
        return (root, query, cb)
                -> factoryId == null ? null : cb.equal(root.get("factoryId"), factoryId);
    }

    public static Specification<Product> hasWarehouseId(Long warehouseId) {
        return (root, query, cb)
                -> warehouseId == null ? null : cb.equal(root.get("warehouseId"), warehouseId);
    }

}
