package org.yihao.deliveryserver.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskStatus {
    ASSIGNED, PICKED_UP, DELIVERED, RETURNED;
    @JsonCreator
    public static DriverStatus fromString(String value) {
        return DriverStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
