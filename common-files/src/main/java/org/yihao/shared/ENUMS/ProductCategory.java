package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductCategory {
    PAD, LAPTOP, MONITOR, WATCH, PHONE;

    @JsonCreator
    public static ProductCategory fromString(String value) {
        return ProductCategory.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
