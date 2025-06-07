package org.yihao.ordermanagementserver.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductPhase {
    DEVELOPING, PRODUCTION, TEST;

    @JsonCreator
    public static ProductPhase fromString(String value) {
        return ProductPhase.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
