package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeliveryType {
    PICKUP,  // Driver picks up the product from the supplier’s factory
    RETURN;   // Driver returns the product to the supplier

    @JsonCreator
    public static DeliveryType fromString(String value) {
        return DeliveryType.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
