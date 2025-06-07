package org.yihao.facilityserver.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.WarehouseStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseId;

    private String warehouseName;

    /*@OneToOne — Defines a one-to-one relationship between Warehouse and Address.
    cascade = CascadeType.ALL — This is the key part.
    It tells JPA: “Any persistence operation (save, update, delete, etc.) performed on Warehouse
    should also be cascaded to its associated Address.”
    So if you call warehouseRepository.save(warehouse),
    and the warehouse object has an unsaved address attached, the address will be automatically saved.
    @JoinColumn(name = "address_id") — Specifies the foreign key column in the warehouse table
    that points to the address table.*/

    //without oprphanRemoval, address still can be updated, but the old address won't be deleted
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private WarehouseStatus status;

    private String contactNumber;

}
