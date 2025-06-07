package org.yihao.ordermanagementserver.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductCategory {
    MEN,WOMEN,KID,UNISEX,BABY;

    @JsonCreator
    public static ProductCategory fromString(String value) {
        return ProductCategory.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
