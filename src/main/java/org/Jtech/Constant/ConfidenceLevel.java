package org.Jtech.Constant;

/** How much the score can be trusted. Order matters: most trustworthy first. */
public enum ConfidenceLevel {
    HIGH,           // normal display
    MEDIUM,         // "Great match" not allowed
    LOW,            // show a "Limited data" badge
    INSUFFICIENT    // no score; show "Not enough data"
}