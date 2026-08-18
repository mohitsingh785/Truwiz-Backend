package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum SkinType {
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
    public static SkinType fromString(String value){
        return SkinType.valueOf(value.toUpperCase());
    }
}
