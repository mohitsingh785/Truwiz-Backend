package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

public enum AllergySeverity {

    @JsonProperty("mild")
    MILD,        // discomfort: itching, slight redness
    @JsonProperty("moderate")
    MODERATE,    // clear reaction: rash, breakouts that need care
    @JsonProperty("severe")
    SEVERE;      // serious reaction → hard warning + score cap (gate in S.7)

    @JsonCreator
    public static AllergySeverity fromString(String value) {
        return EnumParser.parse(AllergySeverity.class, value, "allergy severity");
    }
}
