package org.yihao.facilityserver.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.yihao.facilityserver.model.Address;
import org.yihao.facilityserver.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    boolean existsWarehouseByWarehouseName(String warehouseName);

    boolean existsWarehouseByAddress(Address address);

    Page<Warehouse> findAll(Specification<Warehouse> specification, Pageable pageDetails);
}
