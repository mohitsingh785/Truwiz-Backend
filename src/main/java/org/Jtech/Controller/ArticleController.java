package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.Jtech.Entity.Article;
import org.Jtech.Model.ArticleResponse;
import org.Jtech.Service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Article Controller
 *
 * Purpose:
 * Exposes REST APIs for retrieving educational and informational
 * articles displayed in the application, including content related
 * to personal care, food safety, and ingredient awareness.
 *
 * Scope:
 * - Fetch all available articles
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This controller handles read-only article operations for the app.
 * Business logic is delegated to the ArticleService layer.
 */

@RestController
@RequestMapping("/v1/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * Retrieve all available articles.
     *
     * Used for:
     * - Displaying educational content in the application
     * - Showing articles related to personal care and food safety
     *
     * Metadata:
     * Added on : 2026-02-06
     * Author   : Mohit Singh
     *
     * Modification Summary
     *
     */
    @Operation(summary = "Get All Articles", description = "Retrieve all Articles ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Articles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ArticleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Article Found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<ArticleResponse> getAllArticle() {


        List<Article> articles = articleService.getAllArticle();


        if (articles.isEmpty()) {
            return ResponseEntity.status(204).body(new ArticleResponse(204, "No Article Found", articles));
        }

        return ResponseEntity.status(200).body(new ArticleResponse(200, "Total Article Found : " + articles.size(), articles));
    }

}
