package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** How a label name was linked to an INCI row. */
public enum MatchMethod {

    @JsonProperty("exact")
    EXACT,        // normalized label name == InciIngredient.normalizedName
    @JsonProperty("synonym")
    SYNONYM,      // matched through InciSynonym.normalizedSynonym
    @JsonProperty("unmatched")
    UNMATCHED,    // no match; inci stays null and the name goes to curation
    @JsonProperty("manual")
    MANUAL;       // set by a curator

    @JsonCreator
    public static MatchMethod fromString(String value) {
        return EnumParser.parse(MatchMethod.class, value, "match method");
    }
}