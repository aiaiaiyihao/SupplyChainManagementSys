package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum WarehouseStatus {
    OPERATIONAL,
    UNDER_MAINTENANCE,
    CLOSED;

    @JsonCreator
    public static WarehouseStatus fromString(String value) {
        return WarehouseStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }


}
