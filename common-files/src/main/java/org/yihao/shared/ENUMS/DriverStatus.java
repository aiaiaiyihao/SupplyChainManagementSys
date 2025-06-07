package org.yihao.shared.ENUMS;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum DriverStatus {
    IDLE,APPOINTED,VACATION,TRAINING;

    @JsonCreator
    public static DriverStatus fromString(String value) {
        return DriverStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
