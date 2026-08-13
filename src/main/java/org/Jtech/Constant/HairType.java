package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum HairType {
    @JsonProperty("normal")
    NORMAL,
    @JsonProperty("dry")
    DRY,
    @JsonProperty("oily")
    OILY,
    @JsonProperty("combination")
    COMBINATION,
    @JsonProperty("sensitive")
    SENSITIVE;


    @JsonCreator
    public static HairType fromString(String value){
        return HairType.valueOf(value.toUpperCase());
    }
}
