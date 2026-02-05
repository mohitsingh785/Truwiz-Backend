package org.Jtech.Service;

import org.Jtech.DTO.*;
import org.Jtech.Model.OpenAIResponse;
import org.Jtech.Model.ProductEvaluationRequest;
import org.Jtech.TrainedModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.Jtech.Constant.Const;
import java.util.Collections;
import static org.Jtech.Util.OpenAiParshing.*;

/**
 * OpenAI Service
 *
 * Purpose:
 * Handles AI-driven processing for product-related workflows, including
 * extraction of product details from unstructured text, product evaluation,
 * usage guidance generation, and chemical classification.
 *
 * Scope:
 * - Extract structured product details from OCR text
 * - Evaluate products based on ingredients and user context
 * - Generate product usage guidance
 * - Identify beneficial and harmful chemicals
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This service encapsulates all interactions with the AI model.
 * Prompt generation and response parsing are delegated to
 * trainer and parser utilities to keep responsibilities separated.
 *
 * Security:
 * API keys retrieved by this service must never be logged
 * or exposed outside trusted service layers.
 */



@Service
public class OpenAIService {

    @Autowired
    private UtilsService utilsService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = Const.apiUrl;
    // Use the ProductDetailTrainer to generate the prompt
    private final ProductDetailTrainer productDetailTrainer = new ProductDetailTrainer();
    private final GoodChemicalsTablePrompt goodChemicalsTablePrompt = new GoodChemicalsTablePrompt();
    private final HarmfulChemicalsTablePrompt harmfulChemicalsTablePrompt = new HarmfulChemicalsTablePrompt();
    private final ProductDescriptionPrompt productDescriptionPrompt = new ProductDescriptionPrompt();
    private final ProductusagePrompt productusagePrompt = new ProductusagePrompt();




    /**
     * Extract structured product details from unstructured OCR text.
     *
     * Used for:
     * - Processing raw OCR output from scanned product labels
     * - Extracting product name, category, chemical list,
     *   expiry date, and price information
     *
     * @param productText unstructured OCR text obtained from product label scanning
     * @return extracted structured product details, or null if extraction fails
     */
    public ProductChemicalResponseDTO  extractProductDetailsFromText(String productText) {
        String apiKey = utilsService.getGptKey(Const.gptModelkey);



        // Generate prompt using ProductDetailTrainer
        String prompt = productDetailTrainer.generatePrompt(productText);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);
        OpenAIChatRequest request = new OpenAIChatRequest(Const.gptModel, Collections.singletonList(message), 1000, 0.7);

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

    /**
     * Generate usage guidance for a product based on evaluation data.
     *
     * Used for:
     * - Providing usage instructions and recommendations
     * - Guiding users on safe and effective product usage
     *
     * @param product product evaluation request containing
     *                ingredient and user context
     * @return product usage guide, or null if generation fails
     */
    public UsageGuideDTO fetchProductUsageGuide(ProductEvaluationRequest product) {
        String apiKey = utilsService.getGptKey(Const.gptModelkey);

        // Generate prompt using ProductDetailTrainer
        String prompt = productusagePrompt.generateprompt(formatProductDetails(product));

        System.out.println(prompt);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);


        // Use low temperature and adjusted top_p for deterministic and accurate results
        OpenAIChatRequest request = new OpenAIChatRequest(Const.gptModel, Collections.singletonList(message), 1000, 0.7);

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

    /**
     * Retrieve beneficial (good) chemicals identified in a product.
     *
     * Used for:
     * - Identifying safe or beneficial chemicals present in a product
     * - Displaying positive ingredient information to users
     *
     * @param goodChemicalRequestDTO request data containing product
     *                              and evaluation context
     * @return beneficial chemical details, or null if none are found
     */
    public GoodChemicalResponseDTO fetchGoodChemicalTable(GoodChemicalRequestDTO goodChemicalRequestDTO) {
        String apiKey = utilsService.getGptKey(Const.gptModelkey);

        // Generate prompt using ProductDetailTrainer
        String prompt = goodChemicalsTablePrompt.generatePrompt(formatGoodChemicalDetails(goodChemicalRequestDTO));

        System.out.println(prompt);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);


        // Use low temperature and adjusted top_p for deterministic and accurate results
        OpenAIChatRequest request = new OpenAIChatRequest(Const.gptModel, Collections.singletonList(message), 1000, 0.7);

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


            return parseGoodChemicalResponse(result);
        }

        System.err.println("No response from OpenAI API.");
        return null;
    }

    /**
     * Retrieve harmful or unsafe chemicals identified in a product.
     *
     * Used for:
     * - Highlighting potentially harmful ingredients
     * - Warning users about unsafe or unsuitable chemicals
     *
     * @param harmfulChemicalRequestDTO request data containing product
     *                                  and evaluation context
     * @return harmful chemical details, or null if none are found
     */
 public HarmfulChemicalResponseDTO fetchHarmfulChemicalTable(HarmfulChemicalRequestDTO harmfulChemicalRequestDTO) {
        String apiKey = utilsService.getGptKey(Const.gptModelkey);

        // Generate prompt using ProductDetailTrainer
        String prompt = harmfulChemicalsTablePrompt.generatePrompt(formatHarmfulChemicalDetails(harmfulChemicalRequestDTO));

        System.out.println(prompt);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);


        // Use low temperature and adjusted top_p for deterministic and accurate results
        OpenAIChatRequest request = new OpenAIChatRequest(Const.gptModel, Collections.singletonList(message), 1000, 0.7);

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


            return parseHarmfulChemicalResponse(result);
        }

        System.err.println("No response from OpenAI API.");
        return null;
    }


    /**
     * Evaluate a product based on ingredient composition and user profile.
     *
     * Used for:
     * - Assessing product suitability for a user
     * - Evaluating safety, compatibility, and overall product quality
     *
     * @param product product evaluation request containing
     *                extracted product and user details
     * @return product evaluation result, or null if evaluation fails
     */
    public ProductEvaluationDTO fetchProductEvaluation(ProductEvaluationRequest product) {
        String apiKey = utilsService.getGptKey(Const.gptModelkey);

        // Generate prompt using ProductDetailTrainer
        String prompt = productDescriptionPrompt.generatePrompt(formatProductDetails(product));

        System.out.println(prompt);

        // Prepare the request payload
        OpenAIChatRequest.Message message = new OpenAIChatRequest.Message("user", prompt);

        // Use low temperature and adjusted top_p for deterministic and accurate results
        OpenAIChatRequest request = new OpenAIChatRequest(Const.gptModel, Collections.singletonList(message), 1000, 0.7);

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






}
