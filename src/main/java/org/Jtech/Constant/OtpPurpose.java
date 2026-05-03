package org.Jtech.Constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public  enum OtpPurpose {
    @JsonProperty("register")
    REGISTER,
    @JsonProperty("login")
    LOGIN,
    @JsonProperty("reset")
    RESET_PASSWORD;

    public static OtpPurpose from(String value) {
        switch (value.toLowerCase()) {
            case "login": return LOGIN;
            case "register": return REGISTER;
            case "reset": return RESET_PASSWORD;
            default: throw new IllegalArgumentException("Invalid purpose");
        }
    }
}
