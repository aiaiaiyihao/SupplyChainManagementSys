package org.yihao.deliveryserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.deliveryserver.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class OrderDTO {
    private Long orderId;

    private Long productId;

    private String productName;

    private Integer productQuantity;

    private Long supplierId;

    private String supplierName;

    private OrderStatus orderStatus;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private BigDecimal perPrice;

    private BigDecimal totalPrice;

    private String currency;

    private Long deliveryId;

    private Long factoryId;

    private Long warehouseId;

}
