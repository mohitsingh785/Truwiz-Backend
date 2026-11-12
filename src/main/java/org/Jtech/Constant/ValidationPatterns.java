package org.Jtech.Constant;

public final class ValidationPatterns {

    private ValidationPatterns() {}

    // Letters (any language, so "L'Oréal" works), digits, spaces and & ' ’ . , + -
    // Must start with a letter or digit; surrounding spaces are allowed and trimmed in the service
    public static final String BRAND_NAME =
            "^\\s*[\\p{L}\\p{N}][\\p{L}\\p{N} &'’.,+\\-]*\\s*$";

    // http:// or https://, a domain like www.cerave.com, optional :port, optional /path?query
    public static final String HTTP_URL =
            "^https?://([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}(:\\d{1,5})?(/\\S*)?$";
}
