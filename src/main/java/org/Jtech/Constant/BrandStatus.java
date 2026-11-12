package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Locale;

public enum BrandStatus {

    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE;

    @JsonCreator
    public static BrandStatus fromString(String value) {
        if (value == null) {
            return null;                                   // let @NotNull report it as a 400
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        for (BrandStatus status : values()) {
            if (status.name().equals(normalized)) {
                return status;
            }
        }
        throw new IllegalArgumentException(
                "Invalid brand status '" + value + "'. Allowed values: active, inactive");
    }
}
