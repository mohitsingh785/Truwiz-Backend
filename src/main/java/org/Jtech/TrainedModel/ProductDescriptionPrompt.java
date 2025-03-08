package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDescriptionPrompt {

    private final List<String> examples;

    public ProductDescriptionPrompt() {
        examples = new ArrayList<>();

        // Add predefined examples with the exact format specified
        examples.add(
                "Analyze the following beauty product and user details. Provide an evaluation in the specified format, ensuring the rating for Skin Suitability and Overall Rating considers the user's weight, BMI, and individual characteristics.\n\n" +
                        "Input Details:\n" +
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
                        "Guidelines:\n" +
                        "1. Skin Suitability Rating:\n" +
                        "   - Base the rating on how the product's chemicals and benefits align with the user's skin type, allergies, weight, and BMI.\n" +
                        "   - Include specific concerns or cautions in the \"Note\" field.\n\n" +
                        "2. Overall Rating:\n" +
                        "   - If the rating is 6 or above, provide a \"Why Buy\" reason.\n" +
                        "   - If the rating is below 6, explain \"Why Not Recommended.\"\n\n" +
                        "Output Format:\n" +
                        "Product Evaluation:\n" +
                        "Harmful Chemicals Rating: [1-10]\n" +
                        "Ingredients: [List harmful chemicals]\n" +
                        "⚫ Concern: [Specific concern]\n\n" +
                        "Good Chemicals Rating: [1-10]\n" +
                        "Ingredients: [List good chemicals]\n" +
                        "Benefit: [Benefits of the good chemicals]\n\n" +
                        "Skin Suitability Rating: [1-10]\n" +
                        "Best For: [Best skin type]\n" +
                        "Note: [Cautions or notes]\n\n" +
                        "Best Age Group:\n" +
                        "Rating: [1-10]\n" +
                        "Ideal For: [Age group]\n" +
                        "Reason: [Why it’s ideal]\n\n" +
                        "Overall Rating:\n" +
                        "Rating: [1-10]\n" +
                        "[Why Buy/Why Not Recommended]: [Reason based on the rating]\n\n" +
                        "Example Output (Above 7):\n" +
                        "Product Evaluation:\n" +
                        "Harmful Chemicals Rating: 5/10\n" +
                        "Ingredients: Alcohol Denat., Fragrance\n" +
                        "⚫ Concern: May irritate sensitive skin or cause dryness over time.\n\n" +
                        "Good Chemicals Rating: 9/10\n" +
                        "Ingredients: Shea Butter, Sunflower Oil\n" +
                        "Benefit: Deeply hydrates and nourishes the skin, helping improve texture and radiance.\n\n" +
                        "Skin Suitability Rating: 8/10\n" +
                        "Best For: Normal to dry skin, especially fair-skinned individuals with no allergies.\n" +
                        "Note: Avoid frequent use if prone to sensitivity or dryness.\n\n" +
                        "Best Age Group:\n" +
                        "Rating: 9/10\n" +
                        "Ideal For: 18–30 years\n" +
                        "Reason: The product’s lightweight formula and hydrating properties are ideal for younger skin.\n\n" +
                        "Overall Rating:\n" +
                        "Rating: 8/10\n" +
                        "Why Buy: Excellent for hydration and nourishment. Safe for fair skin and well-suited for the user’s BMI and skin type.\n\n" +
                        "Example Output (Below 7):\n" +
                        "Product Evaluation:\n" +
                        "Harmful Chemicals Rating: 6/10\n" +
                        "Ingredients: Alcohol Denat., Fragrance\n" +
                        "⚫ Concern: Can cause irritation or dryness for fair-skinned individuals.\n\n" +
                        "Good Chemicals Rating: 7/10\n" +
                        "Ingredients: Shea Butter\n" +
                        "Benefit: Provides some hydration but may not be sufficient for dry or sensitive skin.\n\n" +
                        "Skin Suitability Rating: 6/10\n" +
                        "Best For: Normal skin.\n" +
                        "Note: Not suitable for frequent use by fair-skinned individuals prone to irritation.\n\n" +
                        "Best Age Group:\n" +
                        "Rating: 7/10\n" +
                        "Ideal For: 18–30 years\n" +
                        "Reason: Suitable for hydration but not ideal for long-term use.\n\n" +
                        "Overall Rating:\n" +
                        "Rating: 6/10\n" +
                        "Why Not Recommended: Contains potential irritants and may not suit sensitive skin or long-term hydration needs.\n"
        );
    }

    public String generatePrompt(String inputDetails) {
        // Combine examples with the new input
        StringBuilder promptBuilder = new StringBuilder(
                "Analyze the following beauty product and user details. Provide an evaluation in the specified format, ensuring the rating for Skin Suitability and Overall Rating considers the user's weight, BMI, and individual characteristics.\n\n"
        );

        // Append examples
        for (String example : examples) {
            promptBuilder.append(example).append("\n");
        }

        // Add the provided input details
        promptBuilder.append("Input Details:\n").append(inputDetails).append("\n\n")
                .append("Product Evaluation:\n")
                .append("Harmful Chemicals Rating: \n")
                .append("Ingredients: \n")
                .append("⚫ Concern: \n\n")
                .append("Good Chemicals Rating: \n")
                .append("Ingredients: \n")
                .append("Benefit: \n\n")
                .append("Skin Suitability Rating: \n")
                .append("Best For: \n")
                .append("Note: \n\n")
                .append("Best Age Group:\n")
                .append("Rating: \n")
                .append("Ideal For: \n")
                .append("Reason: \n\n")
                .append("Overall Rating:\n")
                .append("Rating: \n")
                .append("[Why Buy/Why Not Recommended]: \n");

        return promptBuilder.toString();
    }
}
