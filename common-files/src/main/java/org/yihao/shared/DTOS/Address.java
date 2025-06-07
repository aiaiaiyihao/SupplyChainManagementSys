package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.AddressType;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long addressId;

    private AddressType addressType;

    private String line1;
    private String line2;
    private String country;
    private String city;
    private String state;
    private Long zip;

}
