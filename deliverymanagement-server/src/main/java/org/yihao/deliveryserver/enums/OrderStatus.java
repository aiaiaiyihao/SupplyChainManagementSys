package org.yihao.deliveryserver.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    CANCELLED,CREATED,APPROVED,DELIVERING,DELIVERED,REJECTED,HELD;

    @JsonCreator
    public static OrderStatus fromString(String value) {
        return OrderStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
