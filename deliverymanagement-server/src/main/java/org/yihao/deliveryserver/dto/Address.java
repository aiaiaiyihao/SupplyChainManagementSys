package org.yihao.deliveryserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.deliveryserver.enums.AddressType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    private Long addressId;
    private AddressType addressType;

    private String line1;
    private String line2;
    private String country;
    private String city;
    private String state;
    private Long zip;

}

