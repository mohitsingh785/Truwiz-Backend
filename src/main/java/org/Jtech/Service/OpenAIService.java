package org.Jtech.Service;

import org.Jtech.DTO.OpenAIChatRequest;
import org.Jtech.DTO.ProductChemicalResponseDTO;
import org.Jtech.DTO.ProductEvaluationDTO;
import org.Jtech.DTO.UsageGuideDTO;
import org.Jtech.Model.OpenAIResponse;
import org.Jtech.Model.ProductEvaluationRequest;
import org.Jtech.TrainedModel.ProductDescriptionPrompt;
import org.Jtech.TrainedModel.ProductDetailTrainer;
import org.Jtech.TrainedModel.ProductusagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OpenAIService {

    @Autowired
    private HealthCheckService healthCheckService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";
    // Use the ProductDetailTrainer to generate the prompt
    private final ProductDetailTrainer productDetailTrainer = new ProductDetailTrainer();
    private final ProductDescriptionPrompt productDescriptionPrompt = new ProductDescriptionPrompt();
    private final ProductusagePrompt productusagePrompt = new ProductusagePrompt();


    public ProductChemicalResponseDTO  fetchAllChemicals(String productText) {
        String apiKey = healthCheckService.getGptKey("chatgpt-3.5");



        // Generate prompt using ProductDetailTrainer
        String prompt = productDetailTrainer.generatePrompt(productText);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);
        OpenAIChatRequest request = new OpenAIChatRequest("gpt-3.5-turbo", Collections.singletonList(message), 500, 0.7);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIChatRequest> entity = new HttpEntity<>(request, headers);

        // Call OpenAI API
        OpenAIResponse response = restTemplate.postForObject(apiUrl, entity, OpenAIResponse.class);

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            // Get the raw result content from OpenAI
            String result = response.getChoices().get(0).getMessage().getContent().trim();

            // Create a DTO to store the parsed information
            ProductChemicalResponseDTO dto = new ProductChemicalResponseDTO();

            // Parse the OpenAI response to extract key details
            try {
                // Split the result into lines
                String[] lines = result.split("\n");

                for (String line : lines) {
                    // Extract values based on the line prefix
                    if (line.startsWith("Product Name:")) {
                        dto.setProductName(line.replace("Product Name:", "").trim());
                    } else if (line.startsWith("Product Category:")) {
                        dto.setProductCategory(line.replace("Product Category:", "").trim());
                    } else if (line.startsWith("Chemicals:")) {
                        dto.setChemicals(line.replace("Chemicals:", "").trim());
                    } else if (line.startsWith("Expiry Date:")) {
                        dto.setExpiryDate(line.replace("Expiry Date:", "").trim());
                    } else if (line.startsWith("Price:")) {
                        dto.setPrice(line.replace("Price:", "").trim());
                    }
                }

            } catch (Exception e) {
                // Log or handle any parsing exceptions
                System.err.println("Error parsing OpenAI response: " + e.getMessage());
            }

            return dto;
        }


        // Return null if no response
        return null;
    }

    private String formatProductDetails(ProductEvaluationRequest product) {
        StringBuilder sb = new StringBuilder();

        sb.append("Product Name: ").append(product.getProductName()).append("\n");
        sb.append("Expiry Date: ").append(product.getExpiryDate()).append("\n");
        sb.append("Manufacturer: ").append(product.getManufacturer()).append("\n");
        sb.append("Chemicals: ").append(String.join(", ", product.getChemicals())).append("\n");

        sb.append("User Details:\n");
        sb.append("  - Gender: ").append(product.getUserDetails().getGender()).append("\n");
        sb.append("  - Age: ").append(product.getUserDetails().getAge()).append("\n");
        sb.append("  - Skin Type: ").append(product.getUserDetails().getSkinType()).append("\n");
        sb.append("  - Allergies: ").append(product.getUserDetails().getAllergies()).append("\n");
        sb.append("  - Weight: ").append(product.getUserDetails().getWeight()).append("\n");
        sb.append("  - BMI: ").append(product.getUserDetails().getBmi()).append("\n");

        return sb.toString();
    }
    public UsageGuideDTO fetchproductusageguide(ProductEvaluationRequest product) {
        String apiKey = healthCheckService.getGptKey("chatgpt-3.5");

        // Generate prompt using ProductDetailTrainer
        String prompt = productusagePrompt.generateprompt(formatProductDetails(product));

        System.out.println(prompt);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);

        // Use low temperature and adjusted top_p for deterministic and accurate results
        OpenAIChatRequest request = new OpenAIChatRequest("gpt-3.5-turbo", Collections.singletonList(message), 1000, 0.7);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIChatRequest> entity = new HttpEntity<>(request, headers);

        // Call OpenAI API
        OpenAIResponse response = restTemplate.postForObject(apiUrl, entity, OpenAIResponse.class);

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            // Get the raw result content from OpenAI
            String result = response.getChoices().get(0).getMessage().getContent().trim();

            if (result == null || result.isEmpty()) {
                System.err.println("OpenAI returned an empty response.");
                return null;
            }
            System.out.println("--------------------------------------------");
            System.out.println(result);

            // Create a DTO to store the parsed information
            return parseUsageGuideResponse(result);
        }

        System.err.println("No response from OpenAI API.");
        return null;
    }

    public ProductEvaluationDTO fetchProductEvaluation(ProductEvaluationRequest product) {
        String apiKey = healthCheckService.getGptKey("chatgpt-3.5");

        // Generate prompt using ProductDetailTrainer
        String prompt = productDescriptionPrompt.generatePrompt(formatProductDetails(product));

        System.out.println(prompt);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);

        // Use low temperature and adjusted top_p for deterministic and accurate results
        OpenAIChatRequest request = new OpenAIChatRequest("gpt-3.5-turbo", Collections.singletonList(message), 1000, 0.7);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIChatRequest> entity = new HttpEntity<>(request, headers);

        // Call OpenAI API
        OpenAIResponse response = restTemplate.postForObject(apiUrl, entity, OpenAIResponse.class);

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            // Get the raw result content from OpenAI
            String result = response.getChoices().get(0).getMessage().getContent().trim();

            if (result == null || result.isEmpty()) {
                System.err.println("OpenAI returned an empty response.");
                return null;
            }
            System.out.println("--------------------------------------------");
            System.out.println(result);

            // Create a DTO to store the parsed information
            return parseOpenAIResponse(result);
        }

        System.err.println("No response from OpenAI API.");
        return null;
    }


    private ProductEvaluationDTO parseOpenAIResponse(String result) {
        ProductEvaluationDTO dto = new ProductEvaluationDTO();

        try {
            String[] lines = result.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();

                if (line.startsWith("Harmful Chemicals Rating:")) {
                    String value = line.replace("Harmful Chemicals Rating:", "").trim();
                    dto.setHarmfulChemicalsRating(parseInteger(value, 0));
                } else if (line.startsWith("Ingredients:") && dto.getHarmfulIngredients() == null) {
                    dto.setHarmfulIngredients(line.replace("Ingredients:", "").trim());
                } else if (line.startsWith("⚫ Concern:")) {
                    dto.setHarmfulConcern(line.replace("⚫ Concern:", "").trim());
                } else if (line.startsWith("Good Chemicals Rating:")) {
                    String value = line.replace("Good Chemicals Rating:", "").trim();
                    dto.setGoodChemicalsRating(parseInteger(value, 0));
                } else if (line.startsWith("Ingredients:") && dto.getGoodIngredients() == null) {
                    dto.setGoodIngredients(line.replace("Ingredients:", "").trim());
                } else if (line.startsWith("Benefit:")) {
                    dto.setGoodBenefits(line.replace("Benefit:", "").trim());
                } else if (line.startsWith("Skin Suitability Rating:")) {
                    String value = line.replace("Skin Suitability Rating:", "").trim();
                    dto.setSkinSuitabilityRating(parseInteger(value, 0));
                } else if (line.startsWith("Best For:")) {
                    dto.setBestForSkinType(line.replace("Best For:", "").trim());
                } else if (line.startsWith("Note:")) {
                    dto.setSkinNote(line.replace("Note:", "").trim());
                } else if (line.startsWith("Best Age Group:")) {
                    // Handle multi-line case
                    String value = line.replace("Best Age Group:", "").trim();
                    if (value.isEmpty() && i + 1 < lines.length && lines[i + 1].trim().startsWith("Rating:")) {
                        value = lines[++i].replace("Rating:", "").trim();
                    }
                    dto.setBestAgeGroupRating(parseInteger(value, 0));
                } else if (line.startsWith("Ideal For:")) {
                    dto.setIdealAgeGroup(line.replace("Ideal For:", "").trim());
                } else if (line.startsWith("Reason:")) {
                    dto.setAgeGroupReason(line.replace("Reason:", "").trim());
                } else if (line.startsWith("Overall Rating:")) {
                    // Handle multi-line case
                    String value = line.replace("Overall Rating:", "").trim();
                    if (value.isEmpty() && i + 1 < lines.length && lines[i + 1].trim().startsWith("Rating:")) {
                        value = lines[++i].replace("Rating:", "").trim();
                    }
                    dto.setOverallRating(parseInteger(value, 0));
                } else if (line.startsWith("Why Buy:")) {
                    dto.setRecommendation("Why Buy: " + line.replace("Why Buy:", "").trim());
                } else if (line.startsWith("Why Not Recommended:")) {
                    dto.setRecommendation("Why Not Recommended: " + line.replace("Why Not Recommended:", "").trim());
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing OpenAI response: " + e.getMessage());
            e.printStackTrace();
        }

        return dto;
    }

    private UsageGuideDTO parseUsageGuideResponse(String result) {
        UsageGuideDTO usageGuideDTO = new UsageGuideDTO();

        try {
            String[] lines = result.split("\n");

            List<String> howToUse = new ArrayList<>();
            List<String> whenToUse = new ArrayList<>();
            List<String> precautions = new ArrayList<>();
            List<String> storageInstructions = new ArrayList<>();
            List<String> additionalTips = new ArrayList<>();

            String currentSection = null;

            for (String line : lines) {
                line = line.trim();
                // Identify section headers
                if (line.startsWith("How to Use:")) {
                    currentSection = "howToUse";
                } else if (line.startsWith("When to Use:")) {
                    currentSection = "whenToUse";
                } else if (line.startsWith("Precautions:")) {
                    currentSection = "precautions";
                } else if (line.startsWith("Storage Instructions:")) {
                    currentSection = "storageInstructions";
                } else if (line.startsWith("Additional Tips:")) {
                    currentSection = "additionalTips";
                } else if (!line.isEmpty() && currentSection != null) {
                    // Add lines to the appropriate section
                    switch (currentSection) {
                        case "howToUse":
                            howToUse.add(line);
                            break;
                        case "whenToUse":
                            whenToUse.add(line);
                            break;
                        case "precautions":
                            precautions.add(line);
                            break;
                        case "storageInstructions":
                            storageInstructions.add(line);
                            break;
                        case "additionalTips":
                            additionalTips.add(line);
                            break;
                    }
                }
            }

            // Set values in the DTO
            usageGuideDTO.setHowToUse(howToUse);
            usageGuideDTO.setWhenToUse(whenToUse);
            usageGuideDTO.setPrecautions(precautions);
            usageGuideDTO.setStorageInstructions(storageInstructions);
            usageGuideDTO.setAdditionalTips(additionalTips);

        } catch (Exception e) {
            System.err.println("Error parsing OpenAI response for Usage Guide: " + e.getMessage());
            e.printStackTrace();
        }

        return usageGuideDTO;
    }


    private int parseInteger(String value, int defaultValue) {
        try {
            if (value == null || value.isEmpty()) {
                System.err.println("Empty or null value encountered for parsing.");
                return defaultValue;
            }

            // Split to handle cases like "7/10"
            if (value.contains("/")) {
                value = value.split("/")[0].trim();
            }

            // Ensure the value is numeric
            if (!value.matches("^\\d+$")) {
                System.err.println("Non-numeric value encountered: " + value);
                return defaultValue;
            }

            return Integer.parseInt(value);
        } catch (Exception e) {
            System.err.println("Error parsing integer: " + value + " - " + e.getMessage());
            return defaultValue;
        }
    }


}
