package org.yihao.orderserver.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.OrderStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long productId;

    private String productName;

    private Integer productQuantity;

    private Long supplierId;

    private String supplierName;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private BigDecimal perPrice;

    private BigDecimal totalPrice;

    private String currency;

    private Long deliveryId;

    private Long factoryId;

    private Long warehouseId;


/*    private Long warehouseId;
    private Long deliveryAddressId;
    private Long driverId;*/

}
