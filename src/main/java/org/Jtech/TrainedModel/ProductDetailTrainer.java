package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailTrainer {

    private final List<String> examples;

    public ProductDetailTrainer() {
        this.examples = new ArrayList<>();

        examples.add(
                "Text: \"Ultra nourishing body moisturizer packed with Salicylic acid, Neem & Turmeric extracts. It contains AQUA, GLYCERIN, SALICYLIC ACID. Manufactured on 07/2024. Price is 349.00.\"\n" +
                        "Product Name: Ultra Nourishing Body Moisturizer\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: AQUA, GLYCERIN, SALICYLIC ACID\n" +
                        "Expiry Date: 07/2026\n" +
                        "Price: 349.00"
        );

        examples.add(
                "Text: \"A face serum with Vitamin C, Hyaluronic Acid. Price 499.00. Expires in 11/2024.\"\n" +
                        "Product Name: Face Serum\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Vitamin C, Hyaluronic Acid\n" +
                        "Expiry Date: 11/2024\n" +
                        "Price: 499.00"
        );

        examples.add(
                "Text: \"Body moisturizer with Aloe Vera, Cocoa Butter, Salicylic Acid. Best before 24 months from Mfg date: 07/2024. MRP: 349.00\"\n" +
                        "Product Name: Acne Free Body Moisturizer\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Aloe Vera, Cocoa Butter, Salicylic Acid\n" +
                        "Expiry Date: 07/2026\n" +
                        "Price: 349.00"
        );
    }
    public String generatePrompt(String inputText) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append(
                "You are an expert in cosmetic and skincare product label interpretation.\n" +
                        "The OCR text comes from one or more photos captured by a user in a fixed order:\n" +
                        "1️⃣ Front photo – usually contains brand name, product name, and marketing phrases.\n" +
                        "2️⃣ Back photo – often includes usage directions, manufacturing or price info.\n" +
                        "3️⃣ Ingredients photo – lists actual chemical ingredients.\n\n" +

                        "⚠️ The user may not always follow the order correctly:\n" +
                        "- They might capture the **front photo multiple times** or skip the ingredient photo.\n" +
                        "- They might capture **ingredient text multiple times** without any front label.\n" +
                        "- Therefore, the text you receive might contain only front info, only ingredients, or a mix from repeated stages.\n\n" +

                        "Your goal is to extract **accurate structured product details** using all text given — combining intelligently when possible, but never inventing information.\n\n" +

                        "🔹 Rules:\n" +
                        "1. Identify brand names (e.g., Mamaearth, Nivea, WOW, L’Oreal, etc.) and always start 'Product Name' with the brand.\n" +
                        "2. Combine brand + descriptive keywords (e.g., 'SPF 50 Sunscreen', 'Hydrating Face Cream') for realistic naming.\n" +
                        "3. If only front text is available and it mentions 'With Vitamin C' or 'Contains Aloe Vera', treat these as *partial known ingredients*, not the full list.\n" +
                        "4. If ingredient list appears (lines starting with or containing 'Ingredients', 'Contains', 'Composition'), extract all chemicals from it — prefer these over partial mentions.\n" +
                        "5. If there are multiple captures, merge logically (brand + name from front, chemicals from ingredient text).\n" +
                        "6. If no chemicals found anywhere, leave 'Chemicals:' blank.\n" +
                        "7. If text is unrelated or random, leave all fields blank.\n" +
                        "8. Categorize skincare, haircare, cosmetic, or hygiene products as 'Personal Care'. Others → blank.\n" +
                        "9. Never hallucinate brand or chemicals.\n\n" +

                        "🔹 Output strictly in this format:\n" +
                        "Product Name:\n" +
                        "Product Category:\n" +
                        "Chemicals:\n" +
                        "Expiry Date:\n" +
                        "Price:\n" +
                        "Detected Source: [Front / Back / Ingredients / Mixed]\n" +
                        "Confidence: [High / Medium / Low]\n\n" +

                        "🔹 Examples:\n" +

                        "Text: \"FRONT: MAMAEARTH SPF 50 SUNSCREEN HYDRATING CREAM. BACK: Dermatologically tested. INGREDIENTS: Aqua, Shea Butter, Glycerin, Vitamin E. Price 349.00 Best before 12/2025\"\n" +
                        "Product Name: Mamaearth SPF 50 Sunscreen Hydrating Cream\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Aqua, Shea Butter, Glycerin, Vitamin E\n" +
                        "Expiry Date: 12/2025\n" +
                        "Price: 349.00\n" +
                        "Detected Source: Mixed\n" +
                        "Confidence: High\n\n" +

                        "Text: \"FRONT: MAMAEARTH HYDRATING CREAM WITH VITAMIN C AND HYALURONIC ACID. BACK: For all skin types.\"\n" +
                        "Product Name: Mamaearth Hydrating Cream with Vitamin C and Hyaluronic Acid\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Vitamin C, Hyaluronic Acid (partial)\n" +
                        "Expiry Date:\n" +
                        "Price:\n" +
                        "Detected Source: Front\n" +
                        "Confidence: Medium\n\n" +

                        "Text: \"INGREDIENTS: Aqua, Niacinamide, Vitamin E, Shea Butter.\"\n" +
                        "Product Name:\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Aqua, Niacinamide, Vitamin E, Shea Butter\n" +
                        "Expiry Date:\n" +
                        "Price:\n" +
                        "Detected Source: Ingredients\n" +
                        "Confidence: High\n\n" +

                        "Text: \"random blurry text abndjada 123\"\n" +
                        "Product Name:\n" +
                        "Product Category:\n" +
                        "Chemicals:\n" +
                        "Expiry Date:\n" +
                        "Price:\n" +
                        "Detected Source:\n" +
                        "Confidence: Low\n\n" +

                        "Now extract structured details for the following OCR text from one or more photos:\n" +
                        "Text: \"" + inputText + "\"\n" +
                        "Product Name:\n" +
                        "Product Category:\n" +
                        "Chemicals:\n" +
                        "Expiry Date:\n" +
                        "Price:\n" +
                        "Detected Source:\n" +
                        "Confidence:\n"
        );

        return promptBuilder.toString();
    }




}
