package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;
/**
 * Harmful Chemicals Table Prompt
 *
 * Purpose:
 * Generates a structured AI prompt for identifying harmful or
 * controversial chemicals present in a product.
 *
 * Scope:
 * - Defines prompt rules for harmful chemical classification
 * - Suggests safer alternatives where applicable
 * - Uses reference examples to enforce output format
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This class is part of the AI prompt-training layer and is used
 * internally during product safety evaluation. It does not
 * contain business logic and should not be exposed directly.
 */

public class HarmfulChemicalsTablePrompt {
    private final List<String> examples;

    public HarmfulChemicalsTablePrompt() {
        examples = new ArrayList<>();

        // ===== EXAMPLE 1 (Reference Only) =====
        examples.add(
                "EXAMPLE 1 (REFERENCE ONLY - DO NOT REWRITE)\n" +
                        "INPUT DETAILS:\n" +
                        "- Chemicals: Parabens, Sodium Lauryl Sulfate (SLS), Glycerin\n" +
                        "- User Details:\n" +
                        "  - Gender: Female\n" +
                        "  - Age: 26\n" +
                        "  - Skin Type: Sensitive\n" +
                        "  - Scalp Type: Oily\n" +
                        "  - Allergies: None\n" +
                        "  - Weight: 60\n" +
                        "  - BMI: 21.0\n\n" +
                        "EXPECTED OUTPUT:\n" +
                        "1.\n" +
                        "Chemical: Parabens (Methylparaben, Propylparaben)\n" +
                        "Usage: Preservative\n" +
                        "Impact: May disrupt hormones; avoid for sensitive/long-term use\n" +
                        "SafetyLevel: Harmful\n" +
                        "Alternative: Potassium Sorbate\n\n" +
                        "2.\n" +
                        "Chemical: Sodium Lauryl Sulfate (SLS)\n" +
                        "Usage: Foaming agent / surfactant\n" +
                        "Impact: Can strip oils; increases irritation risk on sensitive skin\n" +
                        "SafetyLevel: Risky\n" +
                        "Alternative: Sodium Cocoyl Isethionate\n\n"
        );
    }

    public String generatePrompt(String inputDetails) {
        StringBuilder sb = new StringBuilder();

        // 1) Task & rules FIRST
        sb.append("You are a skincare formulation expert.\n")
                .append("TASK: Produce ONLY a Harmful/Controversial Chemicals list for the given inputs.\n")
                .append("Each chemical MUST follow this exact format and ordering:\n")
                .append("1.\n")
                .append("Chemical: <name>\n")
                .append("Usage: <role/function>\n")
                .append("Impact: <negative effects / risks for the user>\n")
                .append("SafetyLevel: <Risky | Harmful | Moderate>\n")
                .append("Alternative: <safer substitute or '—'>\n\n")

                .append("RULES:\n")
                .append("- Include ONLY harmful or controversial chemicals from the *input chemical list* (e.g., Parabens, SLS, Formaldehyde releasers, Phthalates, Triclosan, etc.). If a chemical is safe/beneficial (e.g., Glycerin), SKIP it.\n")
                .append("- Use ONLY chemicals from the input; do NOT introduce new chemicals.\n")
                .append("- Do NOT add tables, ratings (numbers), or extra sections.\n")
                .append("- Personalize 'Impact' using user profile where relevant (skin/scalp type, age, allergies, etc.).\n")
                .append("- 'SafetyLevel' MUST be exactly one of: Risky, Harmful.\n")
                .append("- 'Alternative' suggests a safer replacement serving the same function; use '—' if none is appropriate.\n")
                .append("- Keep each field concise.\n")
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
