package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FactoryStatus {
    OPENED,CLOSED;

    @JsonCreator
    public static FactoryStatus fromString(String value) {
        return FactoryStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
