package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class SupplierDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long supplierId;

    @NotBlank(message = "Supplier name cannot be empty...")
    private String supplierName;

    private SupplierStatus supplierstatus;

//    @Valid // Ensures addresses inside the list are validated
    //@NotBlank only works on Strings.
    @NotNull(message = "Address cannot be empty...")
    private Address address;

    private String webPage;

    @Email
    private String email;

    private String phoneNumber;

    private String parentCompany;

//    @NotNull(message = "Industry type cannot be null")
    private IndustryType industryType;

    private Integer yearEstablished;

    private String documents;

/*    @Valid
    private List<Factory> factories;*/
}
