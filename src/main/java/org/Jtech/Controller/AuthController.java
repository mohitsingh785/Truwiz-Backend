package org.Jtech.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.Jtech.Constant.OtpPurpose;
import org.Jtech.DTO.CombinedUserDetails;
import org.Jtech.DTO.UserData;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.DTO.UserLoginDTO;
import org.Jtech.Entity.EmailVerify;
import org.Jtech.Entity.OTP;
import org.Jtech.Entity.User;
import org.Jtech.Entity.UserDetails;
import org.Jtech.Model.GetUserIdResponse;
import org.Jtech.Model.OtpResponse;
import org.Jtech.Model.UserAndDetails;
import org.Jtech.Repository.EmailOtpRepository;
import org.Jtech.Repository.OtpRepository;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Repository.UserRepository;
import org.Jtech.Service.*;
import org.Jtech.jwt.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
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
    private UserService userService;


    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailOtpRepository emailOtpRepository;

    /**
     * Authenticate a user using email and password.
     * <p>
     * Used for:
     * - Verifying user credentials
     * - Generating JWT token on successful authentication
     *
     * @param userLoginDTO login request containing email and password
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
                            content = @Content(schema = @Schema(implementation = CombinedUserDetails.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();
        Optional<UserData> userDataOptional = authService.authenticate(email);

        if (userDataOptional.isPresent()) {
            UserData userData = userDataOptional.get();
            Long userId = userData.getUserID();
            UserDetailsDTO userDetailsDTO = userDetailsRepository.userdetaildata(userId);

            String encryptedPassword = userData.getPassword();
            if (utilsService.matchPassword(password, encryptedPassword)) {
                org.springframework.security.core.userdetails.UserDetails userDetails = userService.loadUserByUsername(email);
                String token = this.helper.generateToken(userDetails);
                // Assuming `CombinedUserDetails` constructor takes all required parameters.
                CombinedUserDetails combinedUserDetails = new CombinedUserDetails(
                        token,
                        userId,
                        userData.getUserName(),
                        userData.getEmail(),
                        userData.getPhoneNumber(),
                        userDetailsDTO.getSkinType(),
                        userDetailsDTO.getHairType(),
                        userDetailsDTO.getAge(),
                        userDetailsDTO.getGender(),
                        userDetailsDTO.getSkinColour(),
                        userDetailsDTO.getAllergies(),
                        userDetailsDTO.getBmi(),
                        userDetailsDTO.getWeight()
                );


                // Returning CombinedUserDetails as JSON response.
                return ResponseEntity.ok(combinedUserDetails);
            } else {
                return ResponseEntity.status(401).body("Invalid email or password!");
            }
        } else {
            return ResponseEntity.status(401).body("User doesn't exist.");
        }
    }


    /**
     * Reset a user's password.
     * <p>
     * Used for:
     * - Updating password after successful OTP verification
     *
     * @param id       user identifier
     * @param password new password to be set
     * @return status message indicating reset result
     */
    @Operation(
            summary = "Reset the Password",
            description = "Reset the password of the User",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password reset successfully",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )

    @GetMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam(name = "id") Long id,
            @RequestParam("password") String password) {

        try {
            if (id == null || password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid request parameters.");
            }

            String encryptpass = utilsService.hashPassword(password);

            boolean updated = authService.changeUserPassword(id, encryptpass);
            if (!updated) {
                return ResponseEntity.status(401).body("Invalid email or password!");
            }
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while resetting the password.");
        }
    }


    /**
     * Register a new user along with their profile details.
     * <p>
     * Used for:
     * - Creating a new user account
     * - Storing user profile information such as skin type,
     * hair type, allergies, and health-related data
     *
     * @param userAndDetails request payload containing user
     *                       and user profile details
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
                                            "    \"allergies\": [\"Pollen\"],\n" +
                                            "    \"bmi\": 22.3,\n" +
                                            "    \"weight\": 70.0\n" +
                                            "  }\n" +
                                            "}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User and UserDetails added successfully"),
            @ApiResponse(responseCode = "500", description = "An error occurred while adding the user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{ \"error\": \"An error occurred: error details\" }")
                    ))
    })

    @PostMapping("signup")
    public ResponseEntity<String> registerUser(@RequestBody UserAndDetails userAndDetails) {
        try {
            User user = userAndDetails.getUser();
            String encryptpass = utilsService.hashPassword(userAndDetails.getUser().getPassword());
            user.setPassword(encryptpass);
            UserDetails userDetails = userAndDetails.getUserDetails();

            logger.info("Received request with user details: {}", userDetails);

            // Validate allergies and convert to JSON if needed
            if (userDetails.getAllergies() != null) {
                logger.info("Converting allergies to JSON: {}", userDetails.getAllergies());
                // Ensure the JSON is valid
                String allergiesJson = new ObjectMapper().writeValueAsString(userDetails.getAllergies());
                logger.info("Converted allergies JSON: {}", allergiesJson);
                userDetails.setAllergies(new ObjectMapper().readValue(allergiesJson, List.class));
            }

            // Save the user to the database
            User savedUser = userRepository.save(user);

            // Set the foreign key reference in UserDetails
            userDetails.setUser(savedUser);

            // Save the user details to the database
            userDetailsRepository.save(userDetails);

            return ResponseEntity
                    .status(201) // HTTP 201 Created
                    .body("User and UserDetails added successfully!");
        } catch (Exception e) {
            logger.error("Error occurred while adding user and details", e);
            return ResponseEntity
                    .status(500)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    /**
     * Generate an OTP for password reset.
     * <p>
     * Used for:
     * - Sending OTP to user's registered email
     * - Initiating password reset workflow
     *
     * @param email registered user email
     * @return user identifier and OTP generation status
     */
    @Operation(summary = "Generate OTP for password reset", description = "Generate an OTP for a user's email.")
    @GetMapping("/otp/password-reset")
    public ResponseEntity<GetUserIdResponse> generatePasswordResetOtp(@RequestParam(value = "email", required = true) String email) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info("Received request to generate OTP for email: {}", email);

        // Fetch user ID by email
        Long id = authService.getUserIdByEmail(email);
        if (id == null) {
            logger.warn("No user found for email: {}", email);
            GetUserIdResponse response = new GetUserIdResponse(null, "Email not found", 404);
            return ResponseEntity.status(404).body(response);
        }

        logger.info("Found userId: {}", id);

        // Generate a new OTP
        String generatedOtp = utilsService.generateOtp();
        logger.info("Generated OTP: {}", generatedOtp);

        Optional<OTP> existingOtp = otpRepository.findTopByEmailOrderByUpdatedAtDesc(email);
        String hashOtp = utilsService.hashOtp(generatedOtp);

        OTP otp = existingOtp.orElse(new OTP());
        long now = System.currentTimeMillis();

        if (existingOtp.isPresent()) {
            otp.setUpdatedAt(new Timestamp(now));
        } else {
            otp.setCreatedAt(new Timestamp(now));
        }

        otp.setExpiryTime(new Timestamp(now + 60_000));
        otp.setOtpHash(hashOtp);
        otp.setEmail(email);
        otp.setUsed(false);
        otp.setOtpPurpose(OtpPurpose.RESET_PASSWORD);


        authService.saveOrUpdateOtp(otp);

        String subject = "Your OTP Code";
        String body = "Dear user,\n\nYour OTP code is:" + generatedOtp + "\n\nRegards,\nToxi Scan Teams";

        try {
            emailService.sendOtpEmail(email, subject, body);

        } catch (Exception e) {
            e.printStackTrace();

        }
        // If the user ID is found, return a 200 response
        GetUserIdResponse response = new GetUserIdResponse(email, "OTP sent successfully", 200);
        logger.info("Response sent for email: {} with userId: {}", email, id);
        return ResponseEntity.ok(response);
    }


    /**
     * Generate an OTP for email verification during signup.
     * <p>
     * Used for:
     * - Verifying email ownership before account creation
     *
     * @param email email address to be verified
     * @return OTP generation status
     */
    @Operation(summary = "generate-otp-email-verify", description = "Generate an OTP for a email verification.")
    @GetMapping("/otp/email-verification")
    public ResponseEntity<GetUserIdResponse> generateEmailVerificationOtp(@RequestParam(value = "email", required = true) String email) {
        Logger logger = LoggerFactory.getLogger(this.getClass());


        logger.info("Received request to generate OTP for email: {}", email);
        // Fetch user ID by email
        Long id = authService.getUserIdByEmail(email);
        if (id != null) {
            GetUserIdResponse response = new GetUserIdResponse(email, "Email Already Registered", 409);
            return ResponseEntity.status(409).body(response);
        }

        logger.info("Found userId: {}", id);
        // Generate a new OTP
        String generatedOtp = utilsService.generateOtp();
        logger.info("Generated OTP: {}", generatedOtp);

        Optional<OTP> existingOtp = otpRepository.findTopByEmailOrderByUpdatedAtDesc(email);
        String hashOtp = utilsService.hashOtp(generatedOtp);

        OTP otp = existingOtp.orElse(new OTP());
        long now = System.currentTimeMillis();
        logger.info("Found existingOtp: {}", existingOtp);
        if (existingOtp.isPresent()) {
            otp.setUpdatedAt(new Timestamp(now));
        } else {
            logger.info("Found elseeeeeeeeeeeeeeeeeee: {}", existingOtp);
            otp.setCreatedAt(new Timestamp(now));
        }

        otp.setExpiryTime(new Timestamp(now + 60_000));
        otp.setOtpHash(hashOtp);
        otp.setEmail(email);
        otp.setUsed(false);
        otp.setOtpPurpose(OtpPurpose.REGISTER);
        authService.saveOrUpdateOtp(otp);

        String subject = "Your OTP Code";
        String body = "Dear user,\n\nYour OTP code is:" + generatedOtp + "\n\nRegards,\nToxi Scan Teams";

        try {
            emailService.sendOtpEmail(email, subject, body);

        } catch (Exception e) {
            e.printStackTrace();

        }
        // If the user ID is found, return a 200 response
        GetUserIdResponse response = new GetUserIdResponse(email, "OTP sent successfully", 200);
        logger.info("Response sent for email: {} with userId: {}", email);
        return ResponseEntity.ok(response);
    }


    /**
     * Verify an OTP for password reset and Register.
     * <p>
     * Used for:
     * verify the otp when User reset password or register
     * @param otp otp
     * @param email email
     * @param otpPurpose type
     * @return verify the otp when User reset password or register
     */
    @Operation(summary = "verify-otp", description = "Verify an OTP for password reset and Register")
    @PostMapping("/otp/verify-otp")
    public ResponseEntity<GetUserIdResponse> verifyOtp(
            @RequestParam String otp,
            @RequestParam String email,
            @RequestParam String otpPurpose
    ) {

        OtpPurpose purpose = OtpPurpose.from(otpPurpose);
        Optional<OTP> optionalOtp =
                authService.getOtpAndCreatedAtByEmail(email, purpose);

        if (optionalOtp.isEmpty()) {
            logger.warn("No OTP found for email: {} and purpose: {}", email, otpPurpose);
            return ResponseEntity.status(404)
                    .body(new GetUserIdResponse(null, "No OTP found", 404));
        }

        OTP otpEntity = optionalOtp.get();
        long now = System.currentTimeMillis();

        // 1. Already used
        if (otpEntity.isUsed()) {
            return ResponseEntity.status(400)
                    .body(new GetUserIdResponse(null, "OTP already used", 400));
        }

        // 2. Expired
        if (now > otpEntity.getExpiryTime().getTime()) {
            return ResponseEntity.status(400)
                    .body(new GetUserIdResponse(null, "OTP expired", 400));
        }

        // 3. Match
        if (!utilsService.matchOtp(otp, otpEntity.getOtpHash())) {
            return ResponseEntity.status(400)
                    .body(new GetUserIdResponse(null, "Invalid OTP", 400));
        }

        // 4. Mark used
        otpEntity.setUsed(true);
        otpRepository.save(otpEntity);

        return ResponseEntity.ok(
                new GetUserIdResponse(email, "OTP verified successfully", 200)
        );
    }


}
