package org.Jtech.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BrandApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {
    public static <T> BrandApiResponse<T> success(String message, T data) {
        return new BrandApiResponse<>(true, message, data, Instant.now());
    }

    public static <T> BrandApiResponse<T> success(String message) {
        return new BrandApiResponse<>(true, message, null, Instant.now());
    }
}