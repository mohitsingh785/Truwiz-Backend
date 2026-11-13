package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** Pregnancy guidance for an ingredient. UNKNOWN = not assessed yet (the default for uncurated rows). */
public enum PregnancyClass {

    @JsonProperty("safe")
    SAFE,
    @JsonProperty("consult")
    CONSULT,
    @JsonProperty("avoid")
    AVOID,
    @JsonProperty("unknown")
    UNKNOWN;

    @JsonCreator
    public static PregnancyClass fromString(String value) {
        return EnumParser.parse(PregnancyClass.class, value, "pregnancy class");
    }
}