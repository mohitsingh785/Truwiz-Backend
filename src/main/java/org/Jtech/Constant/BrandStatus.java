package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum BrandStatus {

    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE;

    @JsonCreator
    public static BrandStatus fromString(String value){
        return BrandStatus.valueOf(value.toUpperCase());
    }
}
