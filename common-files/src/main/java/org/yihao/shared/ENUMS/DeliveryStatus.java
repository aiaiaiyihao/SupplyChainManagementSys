package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeliveryStatus {
    CREATED,
    APPOINTED,
    DELIVERING,
    DELIVERED,
    CANCELLED;

    @JsonCreator
    public static DeliveryStatus fromString(String value) {
        return DeliveryStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
