package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ScalpType {
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
    public static ScalpType fromString(String value){
        return ScalpType.valueOf(value.toUpperCase());
    }
}
