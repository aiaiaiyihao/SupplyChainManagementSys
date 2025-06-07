package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MovementType {
    INBOUND,   // Supplier → Warehouse
    OUTBOUND;// Warehouse → Supplier (Return)

    @JsonCreator
    public static MovementType fromString(String value) {
        return MovementType.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
