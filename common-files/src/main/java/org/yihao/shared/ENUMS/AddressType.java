package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AddressType {
    SUPPLIER, FACTORY, WAREHOUSE;

    @JsonCreator
    public static AddressType fromString(String value) {
        return AddressType.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
