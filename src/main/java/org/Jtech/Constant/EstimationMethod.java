package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** Which tier of the concentration estimator produced estimatedPct (paper, Algorithm 2). */
public enum EstimationMethod {

    @JsonProperty("declared")
    DECLARED,     // % printed on the pack
    @JsonProperty("range")
    RANGE,        // typical range intersected with label-order bounds
    @JsonProperty("position")
    POSITION;     // no range data: position weight only

    @JsonCreator
    public static EstimationMethod fromString(String value) {
        return EnumParser.parse(EstimationMethod.class, value, "estimation method");
    }
}