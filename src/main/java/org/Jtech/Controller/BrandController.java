package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.Jtech.Constant.BrandStatus;
import org.Jtech.DTO.*;
import org.Jtech.Service.BrandService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


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

    @GetMapping("/{brandId}")
    @Operation(summary = "Get the brand by brandId")
    public ResponseEntity<BrandApiResponse<BrandSummaryResponse>> getBrandById(@PathVariable Long brandId){
        BrandSummaryResponse brand=brandService.getBrandById(brandId);
        return ResponseEntity.ok(BrandApiResponse.success("Brand fetched successfully",brand));
    }

    // Post the brand
    @PostMapping
    @Operation(summary = "Create a new brand")
    public ResponseEntity<BrandApiResponse<BrandSummaryResponse>> createBrand(
            @Valid @RequestBody BrandCreateRequest request) {

        BrandSummaryResponse created = brandService.createBrand(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{brandId}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(BrandApiResponse.success("Brand created successfully", created));
    }

    // Update the brand
    @PutMapping("/{brandId}")
    @Operation(summary = "Update a brand (full replacement: omitted URLs are cleared)")
    public ResponseEntity<BrandApiResponse<BrandSummaryResponse>> updateBrand(
            @PathVariable Long brandId,
            @Valid @RequestBody BrandUpdateRequest request) {

        BrandSummaryResponse updated = brandService.updateBrand(brandId, request);
        return ResponseEntity.ok(BrandApiResponse.success("Brand updated successfully", updated));
    }

    @PatchMapping("/{brandId}/status")
    @Operation(summary = "Activate or deactivate a brand (deactivating hides it from the default list)")
    public ResponseEntity<BrandApiResponse<BrandSummaryResponse>> updateBrandStatus(
            @PathVariable Long brandId,
            @Valid @RequestBody BrandStatusUpdateRequest request) {

        BrandSummaryResponse updated = brandService.updateBrandStatus(brandId, request.status());
        return ResponseEntity.ok(BrandApiResponse.success("Brand status updated successfully", updated));
    }
}
