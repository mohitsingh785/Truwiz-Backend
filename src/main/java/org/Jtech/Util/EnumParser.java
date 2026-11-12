package org.Jtech.Util;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public final class EnumParser {

    private EnumParser() {}

    /**
     * Case-insensitive, null-safe enum lookup for @JsonCreator methods.
     * "very fair", "Very-Fair" and " VERY_FAIR " all resolve to VERY_FAIR.
     */
    public static <E extends Enum<E>> E parse(Class<E> type, String value, String label) {
        if (value == null) {
            return null;                                  // let @NotNull on the DTO report it as a 400
        }
        String normalized = value.trim()
                .toUpperCase(Locale.ROOT)
                .replace(' ', '_')
                .replace('-', '_');

        for (E constant : type.getEnumConstants()) {
            if (constant.name().equals(normalized)) {
                return constant;
            }
        }
        throw new IllegalArgumentException(
                "Invalid " + label + " '" + value + "'. Allowed values: " + allowedValues(type));
    }

    private static <E extends Enum<E>> String allowedValues(Class<E> type) {
        return Arrays.stream(type.getEnumConstants())
                .map(c -> c.name().toLowerCase(Locale.ROOT).replace('_', ' '))
                .collect(Collectors.joining(", "));
    }
}