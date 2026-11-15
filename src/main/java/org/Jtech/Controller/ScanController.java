package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.Jtech.DTO.ScanResponse;
import org.Jtech.Entity.User;
import org.Jtech.Service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/v1/scan")
@Tag(name = "Scan", description = "Scan a product barcode")
public class ScanController {
    // 8 digits (EAN-8) or 12–14 digits (UPC-A, EAN-13, GTIN-14)
    private final String BARCODE_REGEX = "^(\\d{8}|\\d{12,14})$";

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    /**
     * Scans a product by barcode for the authenticated user and returns the product with its ingredients in label order.
     * Returns 400 for an invalid barcode format and 404 (PRODUCT_NOT_FOUND) if no scannable product exists.
     */
    @Operation(
            summary = "Scans a product by barcode",
            description = "Scans a product by barcode for the authenticated user and returns the product with its ingredients in label order",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found; returns product details and ingredients in label order",
                            content = @Content(schema = @Schema(implementation = ScanResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid barcode format (INVALID_BARCODE); must be 8, 12, 13 or 14 digits",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing or expired JWT",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No scannable product for this barcode (PRODUCT_NOT_FOUND)",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping("/{barCode}")
    public ResponseEntity<ScanResponse> scanByBarcode(@PathVariable @Pattern(regexp = BARCODE_REGEX,
            message = "Barcode must be 8, 12, 13 or 14 digits") String barCode,@AuthenticationPrincipal User user){

        ScanResponse results= scanService.scan(barCode,user.getUserId());
        return ResponseEntity.ok().body(new ScanResponse());
    }


}
