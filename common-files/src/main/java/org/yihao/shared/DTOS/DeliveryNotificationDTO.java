package org.yihao.shared.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNotificationDTO {
    private DeliveryDTO deliveryDTO;
    private String supplierName;
    private String driverName;
    private String driverNumber;
    private String supplierEmail;
    private String driverEmail;
    private String productName;
    private Integer productQuantity;
    private Address pickupAddress;
    private Address inventoryAddress;
}
