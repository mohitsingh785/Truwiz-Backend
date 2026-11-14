package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

public enum Concern {

    @JsonProperty("acne")
    ACNE(ConcernArea.SKIN),
    @JsonProperty("dryness")
    DRYNESS(ConcernArea.SKIN),
    @JsonProperty("pigmentation")
    PIGMENTATION(ConcernArea.SKIN),
    @JsonProperty("aging")
    AGING(ConcernArea.SKIN),
    @JsonProperty("sensitivity")
    SENSITIVITY(ConcernArea.SKIN),
    @JsonProperty("oiliness")
    OILINESS(ConcernArea.SKIN),
    @JsonProperty("hair_fall")
    HAIR_FALL(ConcernArea.HAIR),
    @JsonProperty("dandruff")
    DANDRUFF(ConcernArea.HAIR);

    private final ConcernArea area;

    Concern(ConcernArea area) {
        this.area = area;
    }

    public ConcernArea getArea() {
        return area;
    }

    @JsonCreator
    public static Concern fromString(String value) {
        return EnumParser.parse(Concern.class, value, "concern");
    }
}
