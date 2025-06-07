package org.yihao.supplierserver.Repository;

import org.hibernate.annotations.processing.Find;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yihao.supplierserver.Model.Factory;
import org.yihao.supplierserver.Model.Supplier;

import java.util.List;
import java.util.Optional;

public interface FactoryRepository extends JpaRepository<Factory, Long> {

    // ✅ Delete a factory by factoryId but only if it belongs to a specific supplier
    void deleteByFactoryIdAndSupplier_SupplierId(Long factoryId, Long supplierId);

    // ✅ Check if a factory exists for a given supplier
    boolean existsByFactoryIdAndSupplier_SupplierId(Long factoryId, Long supplierId);

    Optional<Factory> findByFactoryIdAndSupplier_SupplierId(Long factoryId, Long supplierId);

    Page<Factory> findFactoriesBySupplier(Supplier supplier, Pageable pageable);

    boolean existsFactoryByFactoryName(String factoryName);
}

