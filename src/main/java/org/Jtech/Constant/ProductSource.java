package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** Where a product record came from. OPEN_BEAUTY_FACTS gets added when 7.1 is built. */
public enum ProductSource {

    @JsonProperty("seeded")
    SEEDED,
    @JsonProperty("user_ocr")
    USER_OCR;

    @JsonCreator
    public static ProductSource fromString(String value) {
        return EnumParser.parse(ProductSource.class, value, "product source");
    }
}