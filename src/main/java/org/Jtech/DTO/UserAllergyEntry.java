package org.Jtech.DTO;

import org.Jtech.Constant.AllergySeverity;

public record UserAllergyEntry(Long allergyId, String allergyName, AllergySeverity severity) {}
