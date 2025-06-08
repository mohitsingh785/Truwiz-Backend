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

        promptBuilder.append("You are an expert product label analyzer.\n")
                .append("Your job is to extract structured product details from unstructured text.\n")
                .append("Only return values that exist in the input text. If a value is missing, leave it blank.\n\n").
                append("IMPORTANT: All products related to skincare, haircare, bodycare, or cosmetics should be categorized as 'Personal Care'. Do not use sub-categories like 'Hair Care' or 'Face Care'.\n\n")
                .append("Use this format:\n")
                .append("Product Name:\n")
                .append("Product Category:\n")
                .append("Chemicals:\n")
                .append("Expiry Date:\n")
                .append("Price:\n\n")
                .append("Examples:\n\n");

        for (String example : examples) {
            promptBuilder.append(example).append("\n\n");
        }

        promptBuilder.append("Now analyze the following:\n")
                .append("Text: \"").append(inputText).append("\"\n")
                .append("Product Name:\n")
                .append("Product Category:\n")
                .append("Chemicals:\n")
                .append("Expiry Date:\n")
                .append("Price:\n");

        return promptBuilder.toString();
    }
}
