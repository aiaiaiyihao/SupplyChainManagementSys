package org.yihao.ordermanagementserver.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.ordermanagementserver.Enum.ProductCategory;
import org.yihao.ordermanagementserver.Enum.ProductPhase;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class ProductDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Exclude from request, include in response

    private Long productId;

    private Long supplierId;

    private Long factoryId;

    private Long warehouseId;

    @NotEmpty
    private String productName;

    private ProductPhase productPhase;

    private BigDecimal productPrice;

    private String currency;

}
