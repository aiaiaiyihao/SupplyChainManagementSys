package org.yihao.supplierserver.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.AddressType;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    //不管是factory address还是supplier address 都属于一个supplier
//    @JsonIgnore


//    @JsonIgnore
//    @OneToOne
//    @JoinColumn(name="supplier_id")
//    private Supplier supplier;

    /*@JsonIgnore
    @OneToOne
    @JoinColumn(name="factory_id")
    private Factory factory;*/

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private String line1;
    private String line2;
    private String country;
    private String city;
    private String state;
    private Long zip;

}
