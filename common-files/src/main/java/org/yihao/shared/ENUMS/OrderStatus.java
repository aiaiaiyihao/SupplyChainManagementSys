package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    CANCELLED,CREATED,APPROVED,APPOINTED,DELIVERING,DELIVERED,REJECTED,HELD,
    RETURNREQUESTED,RETURNAPPROVED,RETURNREJECTED,RETURNAPPOINTED,RETURNDELIVERING,RETURNDELIVERED;

    @JsonCreator
    public static OrderStatus fromString(String value) {
        return OrderStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
