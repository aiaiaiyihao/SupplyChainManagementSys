package org.yihao.productserver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.ProductCategory;
import org.yihao.shared.ENUMS.ProductPhase;


import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull
    private Long supplierId;

    private Long warehouseId;

    private Long factoryId;

    private String productName;

    @Enumerated(EnumType.STRING)
    private ProductPhase productPhase;

    private String productDescription;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private BigDecimal productPrice;

    private String currency;

    private String imageUrl;

}
