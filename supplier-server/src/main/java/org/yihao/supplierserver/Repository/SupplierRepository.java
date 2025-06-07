package org.yihao.supplierserver.Repository;

import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yihao.supplierserver.Model.Supplier;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
    Optional<Supplier> findBySupplierName(String supplierName);
    boolean existsBySupplierName(String supplierName);

    Page<Supplier> findBySupplierId(Long supplierId, Pageable pageDetails);

    boolean existsByEmail(@Email String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
