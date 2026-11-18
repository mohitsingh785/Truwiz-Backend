package org.Jtech.Constant;

/** Which "What's inside" group an ingredient lands in, for one user. */
public enum IngredientGroup {
    GOOD,       // helps your skin or concerns
    CARE,       // a bit risky for you
    HARMFUL,    // bad for you (includes allergy and pregnancy AVOID)
    BASE,       // neither: water, fillers
    UNKNOWN     // not recognised
}