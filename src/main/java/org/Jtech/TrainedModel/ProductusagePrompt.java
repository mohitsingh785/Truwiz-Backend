package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Product Usage Prompt
 *
 * Purpose:
 * Generates an AI prompt for producing a personalized
 * product usage guide based on product details and
 * individual user characteristics.
 *
 * Scope:
 * - Provides usage instructions, timing, precautions, and storage guidance
 * - Personalizes advice using skin/scalp type, age, BMI, weight, and allergies
 * - Ensures consumer-friendly and safety-focused output
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This class belongs to the AI prompt-training layer and is used
 * internally to generate structured usage guidance. It does not
 * perform any validation or business decision-making.
 */


public class ProductusagePrompt {

    private final List<String> examples;

    public ProductusagePrompt() {
        examples = new ArrayList<>();

        // ===== EXAMPLE 1 (REFERENCE ONLY - DO NOT REWRITE) =====
        examples.add(
                "EXAMPLE 1 (REFERENCE ONLY - DO NOT REWRITE)\n" +
                        "INPUT DETAILS (Example):\n" +
                        "- Product Name: Hydrating Face Cream\n" +
                        "- Expiry Date: 12/2025\n" +
                        "- Manufacturer: Skincare Inc.\n" +
                        "- Chemicals: Shea Butter, Sunflower Oil, Alcohol Denat., Fragrance\n" +
                        "- User Details:\n" +
                        "  - Gender: Male\n" +
                        "  - Age: 20\n" +
                        "  - Skin Type: Fair\n" +
                        "  - Scalp Type: Sensitive\n" +
                        "  - Allergies: None\n" +
                        "  - Weight: 75 kg\n" +
                        "  - BMI: 22.5\n\n" +

                        "EXPECTED OUTPUT (Example):\n" +
                        "How to Use:\n" +
                        "- Apply a pea-sized amount to clean, dry skin; massage gently until absorbed.\n" +
                        "- Avoid broken or irritated areas.\n\n" +

                        "When to Use:\n" +
                        "- Morning and night; increase frequency in dry/cold weather.\n\n" +

                        "Precautions:\n" +
                        "- Contains Alcohol Denat. and Fragrance; patch test recommended for sensitive scalp/skin.\n" +
                        "- Avoid eye area; rinse if contact occurs.\n\n" +

                        "Storage Instructions:\n" +
                        "- Keep in a cool, dry place away from sunlight; close lid tightly after use.\n\n" +

                        "Additional Tips:\n" +
                        "- Layer after toner/serum; use sunscreen in daytime.\n" +
                        "- Adjust amount based on BMI/weight if experiencing heaviness or residue.\n"
        );
    }

    public String generateprompt(String inputs) {
        StringBuilder sb = new StringBuilder();

        // 1) Task & Rules
        sb.append("You are a professional skincare advisor.\n")
                .append("TASK: Generate a personalized skincare usage guide for the given product and user.\n\n")

                .append("RULES:\n")
                .append("- Use ONLY the provided inputs (product, chemicals, user details).\n")
                .append("- Personalize recommendations based on skin/scalp type, age, allergies, weight, and BMI.\n")
                .append("- Do NOT copy or paraphrase the reference example; it is for format only.\n")
                .append("- Be concise and consumer-friendly; no marketing claims or unsupported benefits.\n")
                .append("- Output MUST be bounded by BEGIN_OUTPUT and END_OUTPUT markers.\n\n");

        // 2) Show example as reference only, then close the section
        sb.append("=== EXAMPLES (REFERENCE ONLY) START ===\n");
        for (String ex : examples) {
            sb.append(ex).append("\n");
        }
        sb.append("=== EXAMPLES (REFERENCE ONLY) END ===\n\n");

        // 3) Real input after examples
        sb.append("INPUT DETAILS (YOUR CASE):\n")
                .append(inputs)
                .append("\n\n")

                // 4) Output structure instructions
                .append("YOUR TURN: Follow the same section headings as the example, but tailor to the input above.\n")
                .append("Return ONLY the sections below inside the markers. Bulleted lines are fine.\n\n")

                // 5) Output markers (no filled scaffold to prevent echoing)
                .append("BEGIN_OUTPUT\n")
                .append("How to Use:\n")
                .append("When to Use:\n")
                .append("Precautions:\n")
                .append("Storage Instructions:\n")
                .append("Additional Tips:\n")
                .append("END_OUTPUT");

        return sb.toString();
    }
}
