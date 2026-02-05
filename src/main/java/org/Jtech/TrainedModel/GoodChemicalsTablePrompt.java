package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Good Chemicals Table Prompt
 *
 * Purpose:
 * Generates a structured AI prompt for identifying and describing
 * beneficial or neutral chemicals present in a product.
 *
 * Scope:
 * - Provides prompt instructions and formatting rules
 * - Includes reference examples to guide AI output structure
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This class is part of the AI prompt-training layer and is used
 * internally by the OpenAI service. It does not contain business logic
 * and should not be exposed outside the AI processing flow.
 */


public class GoodChemicalsTablePrompt {

    private final List<String> examples;

    public GoodChemicalsTablePrompt() {
        examples = new ArrayList<>();

        // ===== EXAMPLE 1 (Reference Only) =====
        examples.add(
                "EXAMPLE 1 (REFERENCE ONLY - DO NOT REWRITE)\n" +
                        "INPUT DETAILS:\n" +
                        "- Chemicals: Glycerin, Hyaluronic Acid, Niacinamide\n" +
                        "- User Details:\n" +
                        "  - Gender: Male\n" +
                        "  - Age: 24\n" +
                        "  - Skin Type: Oily\n" +
                        "  - Scalp Type: Normal\n" +
                        "  - Allergies: None\n" +
                        "  - Weight: 68\n" +
                        "  - BMI: 22.1\n\n" +
                        "EXPECTED OUTPUT:\n" +
                        "1.\n" +
                        "Chemical: Glycerin\n" +
                        "Usage: Humectant (hydration)\n" +
                        "Impact: Attracts moisture; supports barrier; suits oily skin\n" +
                        "Insight: Widely recognized as safe (FDA, dermatologists)\n\n" +
                        "2.\n" +
                        "Chemical: Hyaluronic Acid\n" +
                        "Usage: Humectant (deep hydration)\n" +
                        "Impact: Plumps and hydrates; lightweight for oily skin\n" +
                        "Insight: Commonly used at 0.1–2% concentration\n\n" +
                        "3.\n" +
                        "Chemical: Niacinamide\n" +
                        "Usage: Barrier support / oil regulation\n" +
                        "Impact: Reduces redness; helps regulate oil; improves texture\n" +
                        "Insight: —\n"
        );
    }

    public String generatePrompt(String inputDetails) {
        StringBuilder sb = new StringBuilder();

        // 1) Task & rules FIRST
        sb.append("You are a skincare formulation expert.\n")
                .append("TASK: Produce ONLY a Good Chemicals list for the given inputs.\n")
                .append("Each chemical MUST follow this exact format and ordering:\n")
                .append("1.\n")
                .append("Chemical: <name>\n")
                .append("Usage: <role/function>\n")
                .append("Impact: <effect on user>\n")
                .append("Insight: <optional research-backed note or '—'>\n\n")

                .append("RULES:\n")
                .append("- Include ONLY beneficial/neutral chemicals from the *input chemical list*. If a chemical is harmful/controversial (e.g., Parabens), SKIP it.\n")
                .append("- Use ONLY chemicals from the input; do NOT add new ones.\n")
                .append("- Do NOT add tables, ratings, alternatives, or extra sections.\n")
                .append("- Personalize 'Impact' using user profile where relevant (skin/scalp type, age, allergies, etc.).\n")
                .append("- Keep each field concise; use '—' when no research-backed insight.\n")
                .append("- Your final answer MUST be between markers BEGIN_OUTPUT and END_OUTPUT.\n")
                .append("- DO NOT COPY ANY LINE FROM THE EXAMPLES BELOW. If you copy, return FORMAT_ERROR instead.\n\n");

                // 2) Show examples as REFERENCE ONLY, then close the section clearly
                sb.append("=== EXAMPLES (REFERENCE ONLY) START ===\n");
                for (String ex : examples) sb.append(ex).append("\n");
                sb.append("=== EXAMPLES (REFERENCE ONLY) END ===\n\n");

                // 3) Provide the real input AFTER the example block
                sb.append("INPUT DETAILS (YOUR CASE):\n")
                        .append(inputDetails)
                        .append("\n\n")

                        // 4) Hard instruction to start fresh output
                        .append("YOUR TURN: Follow the same format as the EXAMPLES, but generate for the input above.\n")
                        .append("Return ONLY the numbered list within the markers below.\n\n")

                        // 5) Only give markers (no filled scaffold to copy)
                        .append("BEGIN_OUTPUT\n")
                        .append("END_OUTPUT");

        return sb.toString();
    }
}
