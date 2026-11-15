package org.Jtech.DTO;

import java.util.List;

public record InciSummaryDto(
        Long inciId,
        String inciName,
        String displayName,
        List<String> tags) {

    public InciSummaryDto {
        tags = tags == null ? List.of() : List.copyOf(tags);
    }
}
