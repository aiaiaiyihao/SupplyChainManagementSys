package org.yihao.supplierserver.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.FactoryStatus;
import org.yihao.supplierserver.Model.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class FactoryDTO {
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Exclude from request, include in response
    private Long factoryId;
    private String factoryName;

    private Address factoryAddress;

    @Enumerated(EnumType.STRING)
    private FactoryStatus factoryStatus;

    private String factoryImagesUrl;
}
