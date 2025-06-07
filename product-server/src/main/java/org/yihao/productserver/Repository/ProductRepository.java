package org.yihao.productserver.Repository;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yihao.productserver.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsProductByProductName(@NotEmpty String productName);

    Page<Product> findProductsBySupplierId(Long supplierId, Pageable pageDetails);

    Page<Product> findAll(Specification<Product> specification, Pageable pageDetails);
}

