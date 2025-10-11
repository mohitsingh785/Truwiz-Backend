
package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDescriptionPrompt {

    private final List<String> examples;

    public ProductDescriptionPrompt() {
        examples = new ArrayList<>();

        examples.add(
                "You are a skincare formulation expert.\n" +
                        "Your job is to evaluate a beauty product based on its chemicals and how it fits a specific user's profile.\n" +
                        "Ratings should strictly reflect the user's personal characteristics such as age, gender, weight, BMI, skin type, allergies, etc.\n\n" +

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

                        "Product Evaluation:\n" +
                        "Harmful Chemicals Rating: 5/10\n" +
                        "Ingredients: Alcohol Denat., Fragrance\n" +
                        "⚫ Concern: May cause mild dryness or sensitivity in fair skin over prolonged use.\n\n" +

                        "Good Chemicals Rating: 9/10\n" +
                        "Ingredients: Shea Butter, Sunflower Oil\n" +
                        "Benefit: Offers excellent hydration and nourishment, ideal for youthful, fair skin.\n\n" +

                        "Skin Suitability Rating: 8/10\n" +
                        "Best For: Fair to normal skin with no known allergies\n" +
                        "Note: Suitable for everyday use, but avoid heavy application on sensitive areas.\n\n" +

                        "Best Age Group:\n" +
                        "Rating: 9/10\n" +
                        "Ideal For: 18–30 years\n" +
                        "Reason: Lightweight texture and fast absorption make it ideal for younger skin.\n\n" +

                        "Overall Rating:\n" +
                        "Rating: 8/10\n" +
                        "Why Buy: Perfect blend of hydration and youth-oriented formulation. Safe for this user's profile with minimal irritation risk.\n"
        );
    }

        public String generatePrompt(String inputDetails) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder
                .append("You are a skincare formulation expert.\n")
                .append("Your job is to evaluate a beauty product based on its chemicals and how it fits a specific user's profile.\n")
                .append("Ratings should strictly reflect the user's personal characteristics such as age, gender, weight, BMI, skin type, allergies, etc.\n\n")

                .append("Analyze the following beauty product and user details. Provide an evaluation in the specified format, ensuring the rating for Skin Suitability and Overall Rating considers the user's weight, BMI, and individual characteristics.\n\n")

                .append("Input Details:\n")
                .append(inputDetails)
                .append("\n\n")

                .append("Guidelines:\n")
                .append("1. Harmful Chemicals Rating:\n")
                .append("   - Rate from 1–10 based on the presence and severity of harmful chemicals (e.g., alcohols, fragrances, sulfates).\n")
                .append("   - More harmful chemicals = lower rating (e.g., 1–3).\n")
                .append("   - Fewer or no harmful chemicals = higher rating (e.g., 8–10).\n\n")


                .append("2. Good Chemicals Rating:\n")
                .append("   - Rate from 1–10 based on the quality and benefits of nourishing ingredients (e.g., shea butter, aloe vera).\n")
                .append("   - Hydrating and antioxidant-rich ingredients = higher rating.\n\n")

                .append("3. Skin Suitability Rating:\n")
                .append("   - Rate from 1–10 based on alignment with user's skin type, allergies, age, weight, BMI.\n")
                .append("   - Mention risks, benefits, or mismatches in the 'Note' section.\n\n")

                .append("4. Best Age Group:\n")
                .append("   - Rate from 1–10 depending on how appropriate the formula is for the user’s age range.\n")
                .append("   - Younger users need lighter formulas; older users may benefit from anti-aging or richer formulas.\n\n")

                .append("5. Overall Rating:\n")
                .append("   - MUST follow this logic:\n")
                .append("     ➤ If NOT recommended, rating must be 6 or below with explanation in 'Why Not Recommended'.\n")
                .append("     ➤ If recommended, rating must be 7–10 with clear reason in 'Why Buy'.\n\n")

                .append("Output Format:\n")
                .append("Product Evaluation:\n")
                .append("Harmful Chemicals Rating: [1-10]\n")
                .append("Ingredients: [List of harmful chemicals only from input]\n")
                .append("⚫ Concern: [e.g., may irritate dry skin, can cause sensitivity, etc.]\n\n")

                .append("Good Chemicals Rating: [1-10]\n")
                .append("Ingredients: [List of good chemicals only from input]\n")
                .append("Benefit: [e.g., hydrates, nourishes, exfoliates, etc. based on actual chemicals]\n\n")

                .append("Skin Suitability Rating: [1-10]\n")
                .append("Best For: [Best suited skin types based on product and user]\n")
                .append("Note: [Warnings or helpful tips]\n\n")

                .append("Best Age Group:\n")
                .append("Rating: [1-10]\n")
                .append("Ideal For: [User’s age group suitability]\n")
                .append("Reason: [Why this age group is ideal based on product properties]\n\n")

                .append("Overall Rating:\n")
                .append("Rating: [1-10]\n")
                .append("[Why Buy/Why Not Recommended]: [Final recommendation based on product and user compatibility]\n");

        return promptBuilder.toString();
    }
}
