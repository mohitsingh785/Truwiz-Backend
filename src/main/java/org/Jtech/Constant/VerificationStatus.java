package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** How much the product data can be trusted. Separate from ProductStatus (shown or hidden). */
public enum VerificationStatus {

    @JsonProperty("verified")
    VERIFIED,
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("rejected")
    REJECTED;

    @JsonCreator
    public static VerificationStatus fromString(String value) {
        return EnumParser.parse(VerificationStatus.class, value, "verification status");
    }
}