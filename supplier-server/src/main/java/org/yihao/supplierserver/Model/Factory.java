package org.yihao.supplierserver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.AddressType;
import org.yihao.shared.ENUMS.FactoryStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Factory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long factoryId;

    private String factoryName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    //怎么处理
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="address_id")
    private Address factoryAddress;

    @Enumerated(EnumType.STRING)
    private FactoryStatus factoryStatus;

    private String factoryImagesUrl;

    public void setFactoryAddress(Address factoryAddress) {
        factoryAddress.setAddressType(AddressType.FACTORY);
//        factoryAddress.setFactory(this);
//        factoryAddress.setSupplier(this.getSupplier());
        this.factoryAddress = factoryAddress;
    }
}
