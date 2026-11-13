package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

public enum InciStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE;

    @JsonCreator
    public static InciStatus fromString(String value) {
        return EnumParser.parse(InciStatus.class, value, "INCI status");
    }
}
