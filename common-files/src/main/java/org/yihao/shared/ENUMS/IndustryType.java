package org.yihao.shared.ENUMS;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum IndustryType {
    AUTOMOTIVE, ELECTRONICS, TEXTILE;

    /*@JsonCreator is for JSON deserialization (Spring/Jackson)
    This allows case-insensitive conversion from "automotive" to IndustryType.AUTOMOTIVE when receiving JSON.
    It only affects how enums are converted from JSON, not how they are stored in the database.*/
    @JsonCreator
    public static IndustryType fromString(String value) {
        return IndustryType.valueOf(value.toUpperCase()); // Case-insensitive mapping
    }
}
