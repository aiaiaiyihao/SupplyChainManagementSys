package org.yihao.driverserver.enums;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum DriverStatus {
    DELIVERING,IDLE,APPOINTED,VACATION,TRAINING;

    @JsonCreator
    public static DriverStatus fromString(String value) {
        return DriverStatus.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
