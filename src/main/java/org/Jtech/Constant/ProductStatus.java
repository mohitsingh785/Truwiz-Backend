package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE;

    @JsonCreator
    public static ProductStatus fromString(String value){
        return ProductStatus.valueOf(value.toUpperCase());
    }
}

