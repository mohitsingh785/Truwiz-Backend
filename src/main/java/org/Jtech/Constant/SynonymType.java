package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** What kind of alternative name an InciSynonym is. */
public enum SynonymType {

    @JsonProperty("common_name")
    COMMON_NAME,      // "Vitamin B3"
    @JsonProperty("trade_name")
    TRADE_NAME,       // a supplier's brand name for the raw material
    @JsonProperty("spelling")
    SPELLING,         // "Aqua/Water/Eau", US vs EU spelling
    @JsonProperty("ocr_variant")
    OCR_VARIANT;      // a known OCR misread, added during curation

    @JsonCreator
    public static SynonymType fromString(String value) {
        return EnumParser.parse(SynonymType.class, value, "synonym type");
    }
}