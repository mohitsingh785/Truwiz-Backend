package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.Jtech.Entity.Category;
import org.Jtech.Model.CategoryResponse;
import org.Jtech.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


/**
 * Category Controller
 *
 * Purpose:
 * Exposes REST APIs for retrieving product and content categories
 * used across the application.
 *
 * Scope:
 * - Fetch all available categories
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This controller handles read-only category operations.
 * Business logic is delegated to the CategoryService layer.
 */


@RestController()
@RequestMapping("/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * Retrieve all available product categories.
     *
     * Used for:
     * - Displaying category lists in the application
     *
     * Metadata:
     * Added on : 2026-02-06
     * Author   : Mohit Singh
     */
    @Operation(
            summary = "Get all categories",
            description = "Retrieve all available product categories",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categories retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No categories found"
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<CategoryResponse> getAllCategories() {


        List<Category> category = categoryService.getAllCategory();


        if (category.isEmpty()) {
            return ResponseEntity.status(204).body(new CategoryResponse(204, "No Category Found", category));
        }

        return ResponseEntity.status(200).body(new CategoryResponse(200, "Total Category Found : " + category.size(), category));
    }

}
