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
import org.Jtech.Constant.OtpPurpose;
import org.Jtech.DTO.*;
import org.Jtech.Entity.OTP;
import org.Jtech.Model.GetOtpResponse;
import org.Jtech.Model.UserAndDetails;
import org.Jtech.Repository.OtpRepository;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Service.*;
import org.Jtech.jwt.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Authentication Controller
 * <p>
 * Purpose:
 * Exposes REST APIs for user authentication and account security,
 * including login, signup, password reset, and OTP-based verification.
 * <p>
 * Scope:
 * - User login with JWT token generation
 * - User registration
 * - Password reset workflows
 * - Email and OTP verification
 * <p>
 * Metadata:
 * Added on : 2025-12-29
 * Author   : Mohit Singh
 * <p>
 * Notes:
 * This controller is responsible only for authentication
 * and account security operations. User profile and
 * product-related logic are handled by separate controllers.
 * Modifition
 * Updated otp sending and verification method with new logic (03-May-2026, Mohit Singh)
 */


@RestController
@RequestMapping("/v1/auth")
@Tag(
        name = "Authentication",
        description = "User authentication and JWT token management"
)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private EmailService emailService;


    /**
     * Authenticate a user using email and password.
     * <p>
     * Used for:
     * - Verifying user credentials
     * - Generating JWT token on successful authentication
     *
     * @param loginRequest login request containing email and password
     * @return authenticated user details along with JWT token
     */
    @Operation(
            summary = "Login using email and password",
            description = "Login using email and password and get all user details",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logged in successfully",
                            content = @Content(schema = @Schema(implementation = LoginRequest.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invalid email or password.",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticate(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }


    /**
     * Reset a user's password.
     * <p>
     * Used for:
     * - Updating password after successful OTP verification
     *
     * @return status message indicating reset result
     */
    @Operation(
            summary = "Reset the Password",
            description = "Reset the password of the User",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password updated successfully",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    @PostMapping("/password/reset")
    public ResponseEntity<org.Jtech.DTO.ApiResponse> resetPassword(
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authService.passwordReset(passwordResetRequest);
        return ResponseEntity.ok().body(new org.Jtech.DTO.ApiResponse(true, "Password updated successfully"));
    }


    /**
     * Register a new user along with their profile details.
     * <p>
     * Used for:
     * - Creating a new user account
     * - Storing user profile information such as skin type,
     * hair type, allergies, and health-related data
     *
     * @param userAndDetailsRequest request payload containing user
     *                              and user profile details
     * @return status message indicating registration result
     */

    @Operation(
            summary = "Add User and Details",
            description = "Add a new user along with their details, including skin type, allergies, and other health-related information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User and UserDetails data to create a new user.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Add User Example",
                                    value = "{\n" +
                                            "  \"user\": {\n" +
                                            "    \"userName\": \"DummyUser\",\n" +
                                            "    \"email\": \"dummysomething@example.com\",\n" +
                                            "    \"password\": \"dummyPassword123\",\n" +
                                            "    \"phoneNumber\": \"9876543210\"\n" +
                                            "  },\n" +
                                            "  \"userDetails\": {\n" +
                                            "    \"hairType\": \"Oily\",\n" +
                                            "    \"skinType\": \"Oily\",\n" +
                                            "    \"age\": 25,\n" +
                                            "    \"gender\": \"MALE\",\n" +
                                            "    \"skinColour\": \"Fair\",\n" +
                                            "    \"allergyIds\": [2,3,4],\n" +
                                            "    \"heightCm\": 122.3,\n" +
                                            "    \"weightKg\": 70.0\n" +
                                            "  }\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Created Successfully"),
            @ApiResponse(responseCode = "500", description = "An error occurred while adding the user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{ \"error\": \"An error occurred: error details\" }")
                    ))
    })

    @PostMapping("signup")
    public ResponseEntity<org.Jtech.DTO.ApiResponse> registerUser(@Valid @RequestBody UserAndDetails userAndDetailsRequest) {
        authService.registerUser(userAndDetailsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new org.Jtech.DTO.ApiResponse(true, "User registered successfully."));
    }

    /**
     * Generate an OTP for password reset.
     * <p>
     * Used for:
     * - Sending OTP to user's registered email
     * - Initiating password reset workflow
     *
     * @return user identifier and OTP generation status
     */
    @Operation(summary = "Generate OTP for password reset", description = "Generate an OTP for a user's email.")
    @PostMapping("/otp/password-reset")
    public ResponseEntity<GetOtpResponse> generatePasswordResetOtp(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        // If the user ID is found, return a 200 response
        GetOtpResponse response = authService.generateOtpForPasswordReset(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok().body(response);
    }


    /**
     * Generate an OTP for email verification during signup.
     * <p>
     * Used for:
     * - Verifying email ownership before account creation
     *
     * @return OTP generation status
     */
    @Operation(summary = "generate-otp-email-verify", description = "Generate an OTP for a email verification.")
    @GetMapping("/otp/email-verification")
    public ResponseEntity<GetOtpResponse> generateEmailVerificationOtp(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {
        GetOtpResponse response = authService.generateOtpForEmailVerification(emailVerificationRequest.getEmail());
        return ResponseEntity.ok().body(response);
    }


    /**
     * Verify an OTP for password reset and Register.
     * <p>
     * Used for:
     * verify the otp when User reset password or register
     *
     * @return verify the otp when User reset password or register
     */
    @Operation(summary = "verify-otp", description = "Verify an OTP for password reset and Register")
    @PostMapping("/otp/verify-otp")
    public ResponseEntity<GetOtpResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest verifyOtpRequest
    ) {
        GetOtpResponse response = authService.verifyOtp(verifyOtpRequest);
        return ResponseEntity.ok().body(response);
    }


}
