package org.Jtech.DTO;
import org.Jtech.Constant.BrandStatus;

public record BrandSummaryResponse(
        Long id,
        String name,
        String websiteUrl,
        String logoUrl,
        BrandStatus status
) {}
