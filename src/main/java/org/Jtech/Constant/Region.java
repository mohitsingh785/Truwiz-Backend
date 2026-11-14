package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

public enum Region {

    @JsonProperty("in")
    IN,     // India: CDSCO / BIS
    @JsonProperty("eu")
    EU,     // European Union: EU Cosmetics Regulation 1223/2009
    @JsonProperty("us")
    US;     // United States: FDA

    @JsonCreator
    public static Region fromString(String value) {
        return EnumParser.parse(Region.class, value, "region");
    }
}
