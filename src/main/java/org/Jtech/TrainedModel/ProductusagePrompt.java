package org.Jtech.TrainedModel;

import java.util.ArrayList;
import java.util.List;

public class ProductusagePrompt {

    private final List<String> list;


    public ProductusagePrompt() {

        list = new ArrayList<>();

        list.add(

                "Analyze the following beauty product details and provide a usage guide in the specified format. and  ensuring the usage guide is in based on considers the user's weight, BMI, and individual characteristics.\n\n" +
                        "Input Details:\n" +
                        "  \"- Product Name: Hydrating Face Cream\\n\" +\n" +
                        "                        \"- Expiry Date: 12/2025\\n\" +\n" +
                        "                        \"- Manufacturer: Skincare Inc.\\n\" +\n" +
                        "                        \"- Chemicals: Shea Butter, Sunflower Oil, Alcohol Denat., Fragrance\\n\" " +
                        "- User Details:\n" +
                        "  - Gender: Male\n" +
                        "  - Age: 20\n" +
                        "  - Skin Type: Fair\n" +
                        "  - Scalp Type: Sensitive\n" +
                        "  - Allergies: None\n" +
                        "  - Weight: 75 kg\n" +
                        "  - BMI: 22.5\n\n" +
                        " Guidelines:\n" +
                        "1. Include a detailed usage guide specifying when and how to use the product.\n" +
                        "2. Provide specific warnings or precautions based on the chemicals in the product.\n" +
                        "3. Include details on compatibility with skin types or environmental conditions (e.g., day/night use, summer/winter suitability).\n" +
                        "4. Structure the output in the given format.\n"+
                "Usage Guide:\n" +
                        "How to Use:\n" +
                        "   - Apply a pea-sized amount of Hydrating Face Cream to clean, dry skin.\n" +
                        "   - Gently massage in circular motions until fully absorbed.\n" +
                        "   - Avoid applying to broken or irritated skin.\n" +
                        "\n" +
                        "2. When to Use:\n" +
                        "   - Best used during the morning and night skincare routine.\n" +
                        "   - Suitable for all seasons but especially beneficial during dry, cold weather.\n" +
                        "\n" +
                        "3. Precautions:\n" +
                        "   - Contains Alcohol Denat. and Fragrance, which may irritate sensitive skin. Perform a patch test before use.\n" +
                        "   - Avoid direct contact with eyes; rinse thoroughly if contact occurs.\n" +
                        "\n"  +
                        "4. Storage Instructions:\n" +
                        "   - Store in a cool, dry place away from direct sunlight.\n" +
                        "   - Ensure the lid is tightly closed after each use to prevent contamination.\n" +
                        "\n" +
                        "5. Additional Tips:\n" +
                        "   - For optimal results, use after applying a toner or serum.\n" +
                        "   - Pair with sunscreen during the daytime to protect skin from UV damage.\n"

        );

    }


  public String generateprompt(String inputs){


        StringBuilder ss=new StringBuilder("Analyze the following beauty product details and provide a usage guide in the specified format.\n\n");


        for(String s : list){
            ss.append(s).append("\n");
        }

      // Add the provided input details
      ss.append("Input Details:\n").append(inputs).append("\n\n")
              .append("Usage Guide: \n")
              .append("How to Use: \n")
              .append("When to Use: \n")
              .append("Precautions: \n")
              .append("Storage Instructions: \n");

      return ss.toString();



  }

}
