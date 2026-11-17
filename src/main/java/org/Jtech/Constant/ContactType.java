package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Jtech.Util.EnumParser;

/** Whether the product stays on the skin/hair or is washed off. Drives exposure in scoring. */
public enum ContactType {

    @JsonProperty("leave_on")
    LEAVE_ON,     // serum, moisturiser, hair oil, leave-in
    @JsonProperty("rinse_off")
    RINSE_OFF;    // face wash, shampoo, conditioner

    @JsonCreator
    public static ContactType fromString(String value) {
        return EnumParser.parse(ContactType.class, value, "contact type");
    }
}