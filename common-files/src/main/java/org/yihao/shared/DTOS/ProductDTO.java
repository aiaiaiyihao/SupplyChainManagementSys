package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class ProductDTO implements Serializable {
    @Serial
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Exclude from request, include in response
    private static final long serialVersionUID = 1L;
    private Long productId;

    private Long supplierId;

    private Long warehouseId;

    private Long factoryId;

    @NotEmpty
    private String productName;

    @NotEmpty
    private ProductPhase productPhase;

    private String productDescription;

    private ProductCategory productCategory;

    private BigDecimal productPrice;

    private String currency;

    private String imageUrl;
}
