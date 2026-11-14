package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.DTO.UserProfileResponse;
import org.Jtech.DTO.UserProfileUpdateRequest;
import org.Jtech.Entity.User;
import org.Jtech.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * User Controller
 *
 * Purpose:
 * Manages user profile and account-related information such as
 * personal details, preferences, and application-specific settings.
 *
 * Scope:
 * - Fetch user profile information
 * - Update user details
 * - Manage user preferences (e.g., allergies, skin/scalp type)
 *
 * Metadata:
 * Created on : 2025-12-29
 * Author     : Mohit Singh
 *
 * Notes:
 * Authentication and password management are intentionally
 * handled outside this controller to maintain separation of concerns.
 */


@RestController
@RequestMapping("/v1/users")
@Tag(
        name = "Users",
        description = "User profile, preference, and personal information management"
)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get the logged-in user's scoring profile.
     * User comes from the JWT; no id in the URL, so nobody can read another user's profile.
     */
    @Operation(
            summary = "Get my profile",
            description = "Skin/scalp type, allergies with severity, concerns, pregnancy, acne-prone and region of the logged-in user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile returned"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal User currentUser) {
        ResponseEntity<UserProfileResponse> ok = ResponseEntity.ok(userService.getMyProfile(currentUser.getUserId()));
        return ok;
    }

    /**
     * Update part of the logged-in user's profile.
     * Missing fields stay unchanged; allergies / concerns replace the whole list ([] = remove all).
     */
    @Operation(
            summary = "Update my profile",
            description = "Send only the fields to change. allergies and concerns replace the full list; [] removes all. "
                    + "severity is optional (existing allergy keeps its severity, new one gets moderate).",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Add new scoring fields",
                                    value = """
                                            {
                                              "pregnant": false,
                                              "acneProne": true,
                                              "region": "in",
                                              "allergies": [
                                                { "allergyId": 2, "severity": "severe" },
                                                { "allergyId": 4 }
                                              ],
                                              "concerns": ["acne", "pigmentation"]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "400", description = "Invalid value or contradicting fields",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "User or allergy not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(
                userService.updateMyProfile(currentUser.getUserId(), currentUser.getEmail(), request));
    }
}
