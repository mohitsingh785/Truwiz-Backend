package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import org.Jtech.Constant.BrandStatus;
import org.Jtech.DTO.BrandApiResponse;
import org.Jtech.DTO.BrandSummaryResponse;
import org.Jtech.DTO.PageResponse;
import org.Jtech.Service.BrandService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

/**
 * Brand Controller
 * <p>
 * Purpose:
 * Used to retrieve the brand information and perform crud operation,
 * <p>
 * Scope:
 * - Get brand based on brandId
 * - Get all the brands
 * - Perform delete, update, insert on the brand
 * - Get all the products linked with the associated brand using the brandId
 * <p>
 * Metadata:
 * Added on : 2026-10-22
 * Author   : Mohit Singh
 * <p>
 * Modifition
 *
 */

@RestController
@RequestMapping("/v1/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // Get the all brands
    @GetMapping
    @Operation(summary = "Get paginated list of brands, sorted by name, optionally filtered by status")
    public ResponseEntity<BrandApiResponse<PageResponse<BrandSummaryResponse>>> getBrands(
            @ParameterObject
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            @RequestParam(required = false) BrandStatus status) {

        PageResponse<BrandSummaryResponse> brands = brandService.getBrands(status, pageable);
        return ResponseEntity.ok(BrandApiResponse.success("Brands fetched successfully", brands));
    }

    // Get the brand by ID

    // Update the Brand

    // Delete the brand


}
