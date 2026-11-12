package org.Jtech.DTO;

import jakarta.validation.constraints.NotNull;
import org.Jtech.Constant.BrandStatus;

public record BrandStatusUpdateRequest(

        @NotNull(message = "Status is required")
        BrandStatus status
) {}