package org.yihao.driverserver.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeliveryStatus {
    CREATED,
    DELIVERING,
    DELIVERED,
    APPOINTED,
    CANCELLED;

    @JsonCreator
    public static DeliveryStatus fromString(String value) {
        return DeliveryStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
