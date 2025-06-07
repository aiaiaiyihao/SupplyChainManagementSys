package org.yihao.facilityserver.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.yihao.facilityserver.model.Warehouse;
import org.yihao.facilityserver.model.Address;
import org.yihao.shared.ENUMS.WarehouseStatus;

public class WarehouseSpecification {
    public static Specification<Warehouse> hasZipcode(Long zipCode) {
            return (Root<Warehouse> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
                if (zipCode == null) {
                    return null; // No filter applied
                }
                /*Warehouse is your main entity.
                Address is a related entity, likely connected with a @OneToOne or @ManyToOne relationship.*/
                Join<Warehouse, Address> addressJoin
                        /*“In the Warehouse entity, join with the address field,
                        so I can access fields from the related Address entity.”*/
                        = root.join("address");
                return cb.equal(addressJoin.get("zip"), zipCode);
            };
    }

    public static Specification<Warehouse> hasStatus(WarehouseStatus status) {
        return (root, query, cb)
                -> status == null ? null : cb.equal(root.get("status"), status);
    }
}
