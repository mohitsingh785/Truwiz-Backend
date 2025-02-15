package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailTrainer {

    private final List<String> examples;

    public ProductDetailTrainer() {
        this.examples = new ArrayList<>();

        // Add predefined examples with complex inputs
        examples.add(
                "Text: \"Ultra nourishing body moisturizer packed with Salicylic acid, Neem & Turmeric extracts helps tackle body acne with its gentle exfoliation. It contains AQUA, HELIANTHUS ANNUUS (SUNFLOWER) SEED OIL, GLYCERIN, SALICYLIC ACID, and more. The product was manufactured on 07/2024 and is best before 24 months from the date of manufacture. The price is 349.00.\"\n" +
                        "Product Name: Ultra Nourishing Body Moisturizer\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: AQUA, HELIANTHUS ANNUUS (SUNFLOWER) SEED OIL, GLYCERIN, SALICYLIC ACID\n" +
                        "Expiry Date: 07/2026\n" +
                        "Price: 349.00\n"
        );

        examples.add(
                "Text: \"This is a face serum containing Vitamin C, Hyaluronic Acid, and Fragrance. The product expires in 11/2024 and costs 499.00.\"\n" +
                        "Product Name: Face Serum\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals: Vitamin C, Hyaluronic Acid, Fragrance\n" +
                        "Expiry Date: 11/2024\n" +
                        "Price: 499.00\n"
        );
        examples.add(
                "Text: \"CAUTION:\n" +
                        "\n" +
                        "For extemal use only.\n" +
                        "\n" +
                        "Do a patch test before use.\n" +
                        "\n" +
                        "Discontinue use if irritation occurs.\n" +
                        "\n" +
                        "Store in a cool and dry place.\n" +
                        "\n" +
                        "SILICONE FREE |\n" +
                        "\n" +
                        "MINERAL OIL FREE\n" +
                        "\n" +
                        "PARABEN FREE\n" +
                        "\n" +
                        "| PEG FREE\n" +
                        "\n" +
                        "Mfg. Lic. No.: TS/MDL/2017-28300\n" +
                        "\n" +
                        "Batch No. :\n" +
                        "\n" +
                        "P72400698\n" +
                        "\n" +
                        "Mfg Date :\n" +
                        "\n" +
                        "07/2024\n" +
                        "\n" +
                        "MRP\n" +
                        "\n" +
                        ":\n" +
                        "\n" +
                        "349.00 1.745\n" +
                        "\n" +
                        "MRP per mi:\n" +
                        "\n" +
                        "Best before 24 months from the date of manufacture Use within 12 months once opened.\n" +
                        "\n" +
                        "Manufactured by: ElPROCES Esthetic Insights Private Limited At Plot No. 16/A, IDA, 4th Phase, Jeedimetla. Hyderabad, Telangana State - 500055, INDIA\n" +
                        "\n" +
                        "Markated by:\n" +
                        "\n" +
                        "Skinncells Seasons Pvt. Ltd.\n" +
                        "\n" +
                        "MIG 2, 550, Road No. 1, KPHB Colorty\n" +
                        "\n" +
                        "Hyderabad, Telangana - 500072. Customer Care Executive No.: +91-90 0236236\n" +
                        "\n" +
                        "Customer Care Address: As Above, Email: hey@skincalls.com\n" +
                        "\n" +
                        "MADE IN INDIADESCRIPTION:\n" +
                        "\n" +
                        "Ultra nourishing body moisturizer packed with Salicylic acid, Neem & Turmeric extracts helps tackle body acne with its gentle exfoliation and unveils hydrated, radiant and smooth textured skin while Aloe vera, Sunflower oil & Cocoa butter replenishes skin dryness, provides hydration and enhances skin texture. It also strengthens the skin barrier and keeps it soft & supple.\n" +
                        "\n" +
                        "INGREDIENTS:\n" +
                        "\n" +
                        "AQUA, HELIANTHUS ANNUUS (SUNFLOWER) SEED OIL, GLYCERIN, CETOSTEARYL ALCOHOL, RICINUS COMMUNIS (CASTOR) SEED OIL, COCOS NUCIFERA (COCONUT) FRUIT OIL, THEOBROMA CACAO (COCOA) SEED BUTTER, SALICYLIC ACID, ALOE BARBADENSIS (ALOE VERA) LEAF JUICE, CURCUMA LONGA (TURMERIC) ROOT EXTRACT, AZADIRACHTA INDICA (NEEM) LEAF EXTRACT, CARBOMER, PHENOXYETHANOL, SORBITAN STEARATE, CETEARYL ALCOHOL, CETEARYL GLUCOSIDE GLYCERYL STEARATE CITRATE, SODIUM GLUCONATE, SODIUM HYDROXIDE, XANTHAN GUM, PERFUME.\n" +
                        "\n" +
                        "DIRECTIONS:\n" +
                        "\n" +
                        "After taking a shower, apply an adequate amount of moisturizer to your body and massage it smoothly unti well absorbed. Use twice a day.ūnderated\n" +
                        "\n" +
                        "Acne Free Body Moisturizer\n" +
                        "\n" +
                        "Cocoa Butter, Salicylic Acid, Neem & Turmeric\n" +
                        "\n" +
                        "Net Qty: 200ml\n\"\n" +
                        "Product Name: Underrated Acne Free Body Moisturizer\n" +
                        "Product Category: Personal Care\n" +
                        "Chemicals:  AQUA, HELIANTHUS ANNUUS (SUNFLOWER) SEED OIL, GLYCERIN, CETOSTEARYL ALCOHOL, RICINUS COMMUNIS (CASTOR) SEED OIL, COCOS NUCIFERA (COCONUT) FRUIT OIL, THEOBROMA CACAO (COCOA) SEED BUTTER, SALICYLIC ACID, ALOE BARBADENSIS (ALOE VERA) LEAF JUICE, CURCUMA LONGA (TURMERIC) ROOT EXTRACT, AZADIRACHTA INDICA (NEEM) LEAF EXTRACT, CARBOMER, PHENOXYETHANOL, SORBITAN STEARATE, CETEARYL ALCOHOL, CETEARYL GLUCOSIDE GLYCERYL STEARATE CITRATE, SODIUM GLUCONATE, SODIUM HYDROXIDE, XANTHAN GUM, PERFUME.\n" +
                        "Expiry Date: 07/2026\n" +
                        "Price:  349.00\n"
        );
    }

    public String generatePrompt(String inputText) {
        // Combine examples with the new input
        StringBuilder promptBuilder = new StringBuilder("Analyze the following text and provide details in this format:\n\n");
        for (String example : examples) {
            promptBuilder.append(example).append("\n");
        }

        // Append the user's input
        promptBuilder.append("Text: \"").append(inputText).append("\"\n")
                .append("Product Name: \n")
                .append("Product Category: \n")
                .append("Chemicals: \n")
                .append("Expiry Date: \n")
                .append("Price: \n");

        return promptBuilder.toString();
    }
}
