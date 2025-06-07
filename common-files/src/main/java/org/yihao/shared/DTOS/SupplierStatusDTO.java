package org.yihao.shared.DTOS;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.SupplierStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStatusDTO {
    @NotBlank(message = "Supplier name cannot be empty...")
    private String supplierName;

    /*@Enumerated is necessary only in entity classes to specify
   how an enum should be stored in the database.*/
    @NotNull(message = "Supplier status cannot be null")
    private SupplierStatus supplierstatus;
}
