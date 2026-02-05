package org.Jtech.Util;

import org.Jtech.DTO.*;
import org.Jtech.Model.GoodChemicalTable;
import org.Jtech.Model.HarmfulChemicalTable;
import org.Jtech.Model.ProductEvaluationRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAI Parsing Utility
 *
 * Purpose:
 * Provides helper methods to parse, format, and safely interpret
 * structured and semi-structured responses returned by OpenAI models.
 *
 * Scope:
 * - Parse AI-generated product evaluation responses
 * - Parse good and harmful chemical tables
 * - Parse usage guides with section detection
 * - Format user and product input into prompt-ready text
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * - This utility assumes AI responses follow a strict prompt-defined format.
 * - All methods are static and stateless.
 * - Errors are logged defensively to avoid breaking the request flow.
 *
 * Warning:
 * Changes to prompt formats MUST be reflected here.
 */



public class OpenAiParshing {

    /**
     * Parses an OpenAI usage guide response into structured sections.
     *
     * Expected Sections:
     * - How to Use
     * - When to Use
     * - Precautions
     * - Storage Instructions
     * - Additional Tips
     *
     * Notes:
     * - Supports BEGIN_OUTPUT / END_OUTPUT markers
     * - Gracefully handles missing or malformed sections
     */
    public static UsageGuideDTO parseUsageGuideResponse(String result) {
        UsageGuideDTO usageGuideDTO = new UsageGuideDTO();

        try {
            String[] lines = result.split("\n");

            List<String> howToUse = new ArrayList<>();
            List<String> whenToUse = new ArrayList<>();
            List<String> precautions = new ArrayList<>();
            List<String> storageInstructions = new ArrayList<>();
            List<String> additionalTips = new ArrayList<>();

            String currentSection = null;
            boolean inOutput = false;

            for (String raw : lines) {
                String line = raw == null ? "" : raw.trim();

                // Boundaries
                if (line.equalsIgnoreCase("BEGIN_OUTPUT")) {
                    inOutput = true;
                    currentSection = null;
                    continue;
                }
                if (line.equalsIgnoreCase("END_OUTPUT")) {
                    break; // stop immediately
                }
                if (!inOutput && (
                        line.startsWith("How to Use:")
                                || line.startsWith("When to Use:")
                                || line.startsWith("Precautions:")
                                || line.startsWith("Storage Instructions:")
                                || line.startsWith("Additional Tips:")
                )) {
                    // In case markers weren't used, allow parsing anyway
                    inOutput = true;
                }
                if (!inOutput) continue;

                // Section headers (case-insensitive)
                String lower = line.toLowerCase();
                if (lower.startsWith("how to use:")) {
                    currentSection = "howToUse";
                    continue;
                } else if (lower.startsWith("when to use:")) {
                    currentSection = "whenToUse";
                    continue;
                } else if (lower.startsWith("precautions:")) {
                    currentSection = "precautions";
                    continue;
                } else if (lower.startsWith("storage instructions:")) {
                    currentSection = "storageInstructions";
                    continue;
                } else if (lower.startsWith("additional tips:")) {
                    currentSection = "additionalTips";
                    continue;
                }

                if (currentSection == null || line.isEmpty()) continue;

                // Clean bullets/numbering like "-", "•", "*", "1.", "2)", etc.
                String cleaned = line
                        .replaceAll("^[-•*]\\s*", "")
                        .replaceAll("^\\d+[\\.)]\\s*", "")
                        .trim();

                if (cleaned.isEmpty()) continue;

                switch (currentSection) {
                    case "howToUse":
                        howToUse.add(cleaned);
                        break;
                    case "whenToUse":
                        whenToUse.add(cleaned);
                        break;
                    case "precautions":
                        precautions.add(cleaned);
                        break;
                    case "storageInstructions":
                        storageInstructions.add(cleaned);
                        break;
                    case "additionalTips":
                        additionalTips.add(cleaned);
                        break;
                    default:
                        // no-op
                }
            }

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

    /**
     * Parses AI-generated good chemical table output into DTO form.
     *
     * Format Assumption:
     * - Numbered blocks (1., 2., 3.)
     * - Each block contains Chemical, Usage, Impact, Insight
     */
    public static GoodChemicalResponseDTO parseGoodChemicalResponse(String result) {
        GoodChemicalResponseDTO responseDTO = new GoodChemicalResponseDTO();
        List<GoodChemicalTable> tableList = new ArrayList<>();

        try {
            String[] lines = result.split("\n");

            GoodChemicalTable current = null;

            for (String line : lines) {
                line = line.trim();

                if (line.matches("^\\d+\\.$")) {
                    // Start of a new chemical block
                    if (current != null) {
                        tableList.add(current);
                    }
                    current = new GoodChemicalTable();
                } else if (line.startsWith("Chemical:")) {
                    if (current == null) current = new GoodChemicalTable();
                    current.setChemical(line.replace("Chemical:", "").trim());
                } else if (line.startsWith("Usage:")) {
                    if (current == null) current = new GoodChemicalTable();
                    current.setUsage(line.replace("Usage:", "").trim());
                } else if (line.startsWith("Impact:")) {
                    if (current == null) current = new GoodChemicalTable();
                    current.setImpact(line.replace("Impact:", "").trim());
                } else if (line.startsWith("Insight:")) {
                    if (current == null) current = new GoodChemicalTable();
                    current.setInsight(line.replace("Insight:", "").trim());
                }
            }

            // Add the last block if present
            if (current != null) {
                tableList.add(current);
            }

            responseDTO.setGoodChemicalTableList(tableList);

        } catch (Exception e) {
            System.err.println("Error parsing Good Chemical response: " + e.getMessage());
            e.printStackTrace();
        }

        return responseDTO;
    }

    /**
     * Parses AI-generated harmful chemical table output into DTO form.
     *
     * Format Assumption:
     * - Numbered blocks (1., 2., 3.)
     * - Each block contains Chemical, Usage, Impact, SafetyLevel, Alternative
     */
    public static HarmfulChemicalResponseDTO parseHarmfulChemicalResponse(String result) {
        HarmfulChemicalResponseDTO responseDTO = new HarmfulChemicalResponseDTO();
        List<HarmfulChemicalTable> tableList = new ArrayList<>();

        try {
            String[] lines = result.split("\n");

            HarmfulChemicalTable current = null;

            for (String line : lines) {
                line = line.trim();

                if (line.matches("^\\d+\\.$")) {
                    // Start of a new chemical block
                    if (current != null) {
                        tableList.add(current);
                    }
                    current = new HarmfulChemicalTable();
                } else if (line.startsWith("Chemical:")) {
                    if (current == null) current = new HarmfulChemicalTable();
                    current.setChemical(line.replace("Chemical:", "").trim());
                } else if (line.startsWith("Usage:")) {
                    if (current == null) current = new HarmfulChemicalTable();
                    current.setUsage(line.replace("Usage:", "").trim());
                } else if (line.startsWith("Impact:")) {
                    if (current == null) current = new HarmfulChemicalTable();
                    current.setImpact(line.replace("Impact:", "").trim());
                } else if (line.startsWith("SafetyLevel:")) {
                    if (current == null) current = new HarmfulChemicalTable();
                    current.setSafetyLevel(line.replace("SafetyLevel:", "").trim());
                }else if (line.startsWith("Alternative:")) {
                    if (current == null) current = new HarmfulChemicalTable();
                    current.setAlternative(line.replace("Alternative:", "").trim());
                }
            }

            // Add the last block if present
            if (current != null) {
                tableList.add(current);
            }

            responseDTO.setHarmfulChemicalTableList(tableList);

        } catch (Exception e) {
            System.err.println("Error parsing Harmful Chemical response: " + e.getMessage());
            e.printStackTrace();
        }

        return responseDTO;
    }

    /**
     * Formats product and user details into a structured text block
     * suitable for OpenAI prompt consumption.
     */
    public static String formatProductDetails(ProductEvaluationRequest product) {
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

    /**
     * Formats good chemical input and user details
     * for AI-based chemical evaluation.
     */
    public static String formatGoodChemicalDetails(GoodChemicalRequestDTO product) {
        StringBuilder sb = new StringBuilder();

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

    /**
     * Formats harmful chemical input and user details
     * for AI-based risk evaluation.
     */
    public static String formatHarmfulChemicalDetails(HarmfulChemicalRequestDTO product) {
        StringBuilder sb = new StringBuilder();

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

    /**
     * Safely parses numeric ratings from AI responses.
     *
     * Handles cases such as:
     * - "7"
     * - "7/10"
     * - Non-numeric or malformed values
     */
    public static int parseInteger(String value, int defaultValue) {
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

    /**
     * Parses a full product evaluation response generated by OpenAI
     * into a strongly typed ProductEvaluationDTO.
     *
     * Handles:
     * - Ratings
     * - Ingredient categorization
     * - Recommendations and concerns
     */
    public static ProductEvaluationDTO parseOpenAIResponse(String result) {
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

}
