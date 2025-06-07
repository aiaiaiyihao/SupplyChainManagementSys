package org.yihao.driverserver.specificaiton;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.yihao.driverserver.model.Driver;
import org.yihao.shared.ENUMS.DriverStatus;
import org.yihao.shared.ENUMS.WarehouseStatus;

public class DriverSpecification {

    public static Specification<Driver> hasStatus(DriverStatus status) {
        return (root, query, cb)
                -> status == null ? null : cb.equal(root.get("driverStatus"), status);
    }
}
