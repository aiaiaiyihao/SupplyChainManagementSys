package org.yihao.supplierserver.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    private String supplierName;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private SupplierStatus supplierstatus;

    private String webPage;

    private String email;

    private String phoneNumber;

    private String parentCompany;

    @Enumerated(EnumType.STRING)
    private IndustryType industryType;

    private Integer yearEstablished;

    private String documents;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Factory> factories = new ArrayList<>();


 /*   public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            address.setSupplier(this);
        }
    }*/

    public void setFactories(List<Factory> factories) {
        this.factories = factories;
        if (!factories.isEmpty()) {
            factories.forEach(f -> f.setSupplier(this));
        }
    }

    public void addFactory(Factory factory) {
        if (factory != null) {
            factory.setSupplier(this);
//            factory.getFactoryAddress().setSupplier(this);
            this.factories.add(factory);
        }
    }
}
