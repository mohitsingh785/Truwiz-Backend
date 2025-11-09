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
                "You are a product label and ingredient extraction expert.\n" +
                        "Your goal is to extract ONLY the factual product details from the given text.\n" +
                        "Do NOT guess or invent information. If a field is missing, leave it completely blank.\n\n" +

                        "Follow these strict rules:\n" +
                        "1. If you don’t find chemicals, leave 'Chemicals:' blank — never add fake names.\n" +
                        "2. If the text contains gibberish or random text (like 'abndjada'), leave all fields blank except 'Text'.\n" +
                        "3. Categorize only skincare, haircare, or cosmetic items as 'Personal Care'.\n" +
                        "   Other unrelated items must have 'Product Category:' blank.\n" +
                        "4. Use the exact chemical names appearing in the text. Do not expand or normalize.\n\n" +

                        "Output strictly in this format:\n" +
                        "Product Name:\n" +
                        "Product Category:\n" +
                        "Chemicals:\n" +
                        "Expiry Date:\n" +
                        "Price:\n\n" +

                        "Example:\n" +
                        "Text: \"Face serum with Vitamin C and Hyaluronic Acid. Price 499.00 Expires 11/2024\"\n" +
                        "Product Name: Face Serum\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Vitamin C, Hyaluronic Acid\n" +
                        "Expiry Date: 11/2024\n" +
                        "Price: 499.00\n\n" +

                        "Now extract details for the following input:\n" +
                        "Text: \"" + inputText + "\"\n"
                
        );

        return promptBuilder.toString();
    }

}
