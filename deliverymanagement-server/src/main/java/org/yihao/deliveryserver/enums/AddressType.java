package org.yihao.deliveryserver.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AddressType {
    SUPPLIER, FACTORY;

    @JsonCreator
    public static AddressType fromString(String value) {
        return AddressType.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
