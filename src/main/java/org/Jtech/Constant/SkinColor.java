package org.Jtech.Constant;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum SkinColor {
    @JsonProperty("very fair")
    VERY_FAIR,
    @JsonProperty("fair")
    FAIR,
    @JsonProperty("light")
    LIGHT,
    @JsonProperty("medium")
    MEDIUM,
    @JsonProperty("tan")
    TAN,
    @JsonProperty("brown")
    BROWN,
    @JsonProperty("dark brown")
    DARK_BROWN,
    @JsonProperty("deep")
    DEEP;

    @JsonCreator
    public static SkinColor fromString(String value){return SkinColor.valueOf(value.toUpperCase());}
}
