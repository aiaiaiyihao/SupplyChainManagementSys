package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    MANAGER,SUPPLIER,DRIVER;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
