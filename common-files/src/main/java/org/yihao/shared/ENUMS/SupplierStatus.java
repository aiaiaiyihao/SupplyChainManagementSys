package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SupplierStatus {
    ONBOARD,OFFBOARD;

    /*@JsonCreator is for JSON deserialization (Spring/Jackson)
    This allows case-insensitive conversion from "automotive" to IndustryType.AUTOMOTIVE when receiving JSON.
    It only affects how enums are converted from JSON, not how they are stored in the database.*/
    @JsonCreator
    public static SupplierStatus fromString(String value) {
        return SupplierStatus.valueOf(value.toUpperCase()); // Convert to uppercase to avoid case mismatch
    }
//    @JsonValue
//    public String toJson() {
//        return this.name();
//    }
}
