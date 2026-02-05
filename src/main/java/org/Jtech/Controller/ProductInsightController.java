package org.Jtech.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.Jtech.DTO.*;
import org.Jtech.Model.ProductEvaluationRequest;
import org.Jtech.Service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product Insight Controller
 *
 * Purpose:
 * Exposes REST APIs for analyzing products and generating insights
 * based on ingredient and chemical information.
 *
 * Scope:
 * - Analyze product ingredients and chemicals
 * - Provide safety and composition insights during product scanning
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * These APIs are primarily used during product scanning
 * and analysis flows in the application. AI-based processing
 * is delegated to the service layer.
 */


@RestController
@RequestMapping("/v1/products")
public class ProductInsightController {

    @Autowired
    private OpenAIService openAIService;

    /**
     * Extract structured product details from unstructured OCR text.
     *
     * Used for:
     * - Processing raw OCR output from scanned product labels
     * - Extracting product name, category, chemical list,
     *   expiry date, and price information
     *
     * @param productText unstructured OCR text obtained from product label scanning
     * @return extracted structured product details
     */
    @Operation(
            summary = "Extract product details from text",
            description = "Extract structured product details such as name, category, chemicals, expiry date, and price from unstructured OCR text",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product details extracted successfully",
                            content = @Content(schema = @Schema(implementation = ProductChemicalResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unable to extract product details from provided text"
                    )
            }
    )

    @PostMapping("extract-details")
    public ResponseEntity<?> extractProductDetails(@RequestBody String productText) {
        ProductChemicalResponseDTO responseDTO = openAIService.extractProductDetailsFromText(productText);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Unable to extract product details");
        }

        return ResponseEntity.ok(responseDTO);
    }


    /**
     * Evaluate a product based on user profile and ingredient information.
     *
     * Used for:
     * - Assessing product suitability for a user
     * - Evaluating safety based on ingredients, allergies, and preferences
     *
     * @param request product and user evaluation request data
     * @return product evaluation result
     */
    @Operation(
            summary = "Evaluate product suitability",
            description = "Evaluate a product based on user profile details and ingredient information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product evaluation completed successfully",
                            content = @Content(schema = @Schema(implementation = ProductEvaluationDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unable to evaluate product"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ProductEvaluationRequest.class))
            )
    )
    @PostMapping("/evaluation")
    public ResponseEntity<?> evaluateProductSuitability(@RequestBody ProductEvaluationRequest request) {
        ProductEvaluationDTO responseDTO = openAIService.fetchProductEvaluation(request);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Unable to evaluate product");
        }

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Generate usage guidance for a product based on evaluation data.
     *
     * Used for:
     * - Providing usage instructions and recommendations
     * - Guiding users on safe and effective product usage
     *
     * @param request product evaluation data used to generate usage guidance
     * @return product usage guide and recommendations
     */
    @Operation(
            summary = "Generate product usage guide",
            description = "Generate usage guidance and recommendations based on product evaluation data",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product usage guide generated successfully",
                            content = @Content(schema = @Schema(implementation = UsageGuideDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Unable to generate usage guide"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ProductEvaluationRequest.class))
            )
    )
    @PostMapping("/usage-guide")
    public ResponseEntity<?> generateProductUsageGuide(@RequestBody ProductEvaluationRequest request) {
        UsageGuideDTO responseDTO = openAIService.fetchProductUsageGuide(request);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Unable to generate usage guide");
        }

        return ResponseEntity.ok(responseDTO);
    }



    /**
     * Retrieve beneficial (good) chemicals for a product to show it as table in UI.
     *
     * Used for:
     * - Identifying safe or beneficial chemicals present in a product
     * - Displaying positive ingredient information to users
     *
     * @param goodChemicalRequestDTO request data containing product
     *                              and evaluation context
     * @return list of beneficial chemicals with usage information
     */
    @Operation(
            summary = "Get Good Chemical Usage Table Data",
            description = "Provides Good Chemical Usage Table Data  based on good chemical evaluation data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Good Chemical Usage Table Data provided",
                            content = @Content(schema = @Schema(implementation = GoodChemicalResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Good Chemical Usage Table Data found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = GoodChemicalRequestDTO.class))
            )
    )
    @PostMapping("/chemicals/good")
    public ResponseEntity<?> getGoodChemicals(@RequestBody GoodChemicalRequestDTO goodChemicalRequestDTO){

        GoodChemicalResponseDTO responseDTO = openAIService.fetchGoodChemicalTable(goodChemicalRequestDTO);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No beneficial chemicals found");
        }
        return ResponseEntity.ok(responseDTO);
    }


    /**
     * Retrieve harmful or unsafe chemicals for a product.
     *
     * Used for:
     * - Identifying chemicals that may be unsafe or unsuitable for a user
     * - Highlighting potentially harmful ingredients in a product
     *
     * @param harmfulChemicalRequestDTO request data containing product
     *                                  and evaluation context
     * @return list of harmful chemicals with risk information
     */
    @Operation(
            summary = "Get harmful product chemicals",
            description = "Retrieve harmful or unsafe chemicals identified in a product based on evaluation data",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Harmful chemicals retrieved successfully",
                            content = @Content(schema = @Schema(implementation = HarmfulChemicalResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No harmful chemicals found"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = HarmfulChemicalRequestDTO.class))
            )
    )
    @PostMapping("/chemicals/harmful")
    public ResponseEntity<?> getHarmfulChemicals(
            @RequestBody HarmfulChemicalRequestDTO harmfulChemicalRequestDTO) {

        HarmfulChemicalResponseDTO responseDTO =
                openAIService.fetchHarmfulChemicalTable(harmfulChemicalRequestDTO);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No harmful chemicals found");
        }

        return ResponseEntity.ok(responseDTO);
    }

}
