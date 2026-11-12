package org.Jtech.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.Jtech.Constant.ValidationPatterns;

public record BrandCreateRequest(


        @NotBlank(message = "Brand name is required")
        @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
        @Pattern(regexp = ValidationPatterns.BRAND_NAME,
                message = "Brand name can only contain letters, numbers, spaces and & ' . , + -")
        String name,

        @Size(max = 255, message = "Website URL must not exceed 255 characters")
        @Pattern(regexp = ValidationPatterns.HTTP_URL,
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Website URL must be a valid http or https URL")
        String websiteUrl,

        @Size(max = 255, message = "Logo URL must not exceed 255 characters")
        @Pattern(regexp = ValidationPatterns.HTTP_URL,
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Logo URL must be a valid http or https URL")
        String logoUrl
) {
}
