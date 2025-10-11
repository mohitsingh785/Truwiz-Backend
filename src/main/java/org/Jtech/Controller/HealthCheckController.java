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
import org.Jtech.DTO.*;
import org.Jtech.jwt.JwtHelper;
import org.Jtech.Model.*;
import org.Jtech.Repository.EmailOtpRepository;
import org.Jtech.Service.EmailService;
import org.Jtech.Entity.*;
import org.Jtech.Repository.OtpRepository;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Repository.UserRepository;
import org.Jtech.Service.HealthCheckService;
import org.Jtech.Service.OpenAIService;
import org.Jtech.Util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1/healthcheck")
@Tag(name = "Health Check API", description = "APIs for managing user health check-related operations")
public class HealthCheckController {
//    http://localhost:8080/swagger-ui/index.html

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private HealthCheckService healthCheckService;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailOtpRepository emailOtpRepository;
    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private JwtHelper helper;


    @Operation(summary = "Get User Details", description = "Fetch user details by their ID.")
    @GetMapping("userDetail")
    public ResponseEntity<?> getuser(@RequestParam(value = "id", required = false) Long id) {


        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please provide a valid ID.");
        }

        // Fetch user details from the repository
        UserDetailsDTO userDetails = userDetailsRepository.userdetaildata(id);

        // Check if user details are null (invalid ID)
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found for ID: " + id);
        }

        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

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
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();
        Optional<UserData> userDataOptional = healthCheckService.authenticate(email);

        if (userDataOptional.isPresent()) {
            UserData userData = userDataOptional.get();
            Long userId = userData.getUserID();
            UserDetailsDTO userDetailsDTO = userDetailsRepository.userdetaildata(userId);

            String encryptedPassword = userData.getPassword();
            if (passwordUtil.matchPassword(password, encryptedPassword)) {
                org.springframework.security.core.userdetails.UserDetails userDetails = healthCheckService.loadUserByUsername(email);
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
    @GetMapping("/reset-user-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam(name = "id") Long id,
            @RequestParam("password") String password) {

        try {
            if (id == null || password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid request parameters.");
            }

            String encryptpass = passwordUtil.hashPassword(password);

            boolean updated = healthCheckService.changeUserPassword(id, encryptpass);
            if (!updated) {
                return ResponseEntity.status(401).body("Invalid email or password!");
            }
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while resetting the password.");
        }
    }


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

    @PostMapping("adduser")
    public ResponseEntity<String> addUserAndDetails(@RequestBody UserAndDetails userAndDetails) {
        try {
            User user = userAndDetails.getUser();
            String encryptpass = passwordUtil.hashPassword(userAndDetails.getUser().getPassword());
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


    @Operation(summary = "Generate OTP", description = "Generate an OTP for a user's email.")
    @GetMapping("/Generate_otp")
    public ResponseEntity<GetUserIdResponse> getuserIdbyemail(@RequestParam(value = "email", required = true) String email) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info("Received request to generate OTP for email: {}", email);

        // Fetch user ID by email
        Long id = healthCheckService.getuserIdbyemail(email);
        if (id == null) {
            logger.warn("No user found for email: {}", email);
            GetUserIdResponse response = new GetUserIdResponse(null, "Email not found", 404);
            return ResponseEntity.status(404).body(response);
        }

        logger.info("Found userId: {}", id);

        // Generate a new OTP
        String generatedOtp = healthCheckService.generateotp();
        logger.info("Generated OTP: {}", generatedOtp);

        Optional<OTP> existingOtp = otpRepository.findById(id.intValue());

        OTP otp = existingOtp.orElse(new OTP());
        otp.setUserId(id.intValue());
        otp.setOtp(generatedOtp);
        otp.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        healthCheckService.saveOrUpdateOtp(otp);

        String subject = "Your OTP Code";
        String body = "Dear user,\n\nYour OTP code is:" + generatedOtp + "\n\nRegards,\nToxi Scan Teams";

        try {
            emailService.sendOtpEmail(email, subject, body);

        } catch (Exception e) {
            e.printStackTrace();

        }
        // If the user ID is found, return a 200 response
        GetUserIdResponse response = new GetUserIdResponse(id, "OTP sent successfully", 200);
        logger.info("Response sent for email: {} with userId: {}", email, id);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "generate-otp-email-verify", description = "Generate an OTP for a email verification.")
    @GetMapping("/generate-otp-email-verify")
    public ResponseEntity<GetUserIdResponse> verifyEmailbyotp(@RequestParam(value = "email", required = true) String email) {
        Logger logger = LoggerFactory.getLogger(this.getClass());


        logger.info("Received request to generate OTP for email: {}", email);
        // Fetch user ID by email
        Long id = healthCheckService.getuserIdbyemail(email);
        if (id != null) {
            logger.warn("No user found for email: {}", email);
            GetUserIdResponse response = new GetUserIdResponse(id, "Email Already Registered", 409);
            return ResponseEntity.status(409).body(response);
        }

        logger.info("Found userId: {}", id);
        // Generate a new OTP
        String generatedOtp = healthCheckService.generateotp();
        logger.info("Generated OTP: {}", generatedOtp);

        EmailVerify otp = new EmailVerify();
        otp.setOtp(generatedOtp);
        otp.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        emailOtpRepository.save(otp);

        String subject = "Your OTP Code";
        String body = "Dear user,\n\nYour OTP code is:" + generatedOtp + "\n\nRegards,\nToxi Scan Teams";

        try {
            emailService.sendOtpEmail(email, subject, body);

        } catch (Exception e) {
            e.printStackTrace();

        }
        // If the user ID is found, return a 200 response
        GetUserIdResponse response = new GetUserIdResponse((long) otp.getId(), "OTP sent successfully", 200);
        logger.info("Response sent for email: {} with userId: {}", email, (long) otp.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verify OTP", description = "Verify the OTP for a user.")
    @GetMapping("verify_otp")
    public ResponseEntity<GetUserIdResponse> verifyOtp(
            @RequestParam(value = "otp") String otp,
            @RequestParam(value = "id") Integer id) {

        // Fetch OTP and createdAt
        Optional<OtpResponse> result = healthCheckService.getOtpAndCreatedAtByUserId(id);

        if (result.isPresent()) {
            OtpResponse otpResponse = result.get();
            String storedOtp = otpResponse.getOtp();
            Timestamp createdAt = otpResponse.getCreatedAt();

            // Log the fetched data
            logger.info("Fetched OTP for userId {}: {}", id, storedOtp);
            logger.info("Fetched Timestamp for userId {}: {}", id, createdAt);

            // Get the current system time
            long currentTime = System.currentTimeMillis();
            long createdTime = createdAt.getTime();

            // Calculate the time difference
            long timeDifference = (currentTime - createdTime) / 1000; // Difference in seconds

            // Verify OTP and time range
            if (timeDifference <= 60 && storedOtp.equals(otp)) {
                logger.info("OTP verified successfully for userId: {}", id);
                return ResponseEntity.ok(new GetUserIdResponse((long) id, "OTP verified successfully", 200));
            } else if (timeDifference > 60) {
                logger.warn("OTP expired for userId: {}", id);
                return ResponseEntity.status(400).body(new GetUserIdResponse(null, "OTP expired", 400));
            } else {
                logger.warn("Invalid OTP for userId: {}", id);
                return ResponseEntity.status(400).body(new GetUserIdResponse(null, "Invalid OTP", 400));
            }
        } else {
            logger.warn("No OTP found for userId: {}", id);
            return ResponseEntity.status(404).body(new GetUserIdResponse(null, "No OTP found", 404));
        }
    }


    @Operation(summary = "Verify OTP For Signup", description = "Verify the OTP for a user for Signup.")
    @GetMapping("verify_otp_signup")
    public ResponseEntity<GetUserIdResponse> verifyOtpSignup(
            @RequestParam(value = "otp") String otp,
            @RequestParam(value = "id") Integer id) {

        // Fetch OTP and createdAt
        Optional<OtpResponse> result = healthCheckService.getOtpAndEmailVerify(id);

        if (result.isPresent()) {
            OtpResponse otpResponse = result.get();
            String storedOtp = otpResponse.getOtp();
            Timestamp createdAt = otpResponse.getCreatedAt();

            // Log the fetched data
            logger.info("Fetched OTP for userId {}: {}", id, storedOtp);
            logger.info("Fetched Timestamp for userId {}: {}", id, createdAt);

            // Get the current system time
            long currentTime = System.currentTimeMillis();
            long createdTime = createdAt.getTime();

            // Calculate the time difference
            long timeDifference = (currentTime - createdTime) / 1000; // Difference in seconds

            // Verify OTP and time range
            if (timeDifference <= 60 && storedOtp.equals(otp)) {
                logger.info("OTP verified successfully for userId: {}", id);
                return ResponseEntity.ok(new GetUserIdResponse((long) id, "OTP verified successfully", 200));
            } else if (timeDifference > 60) {
                logger.warn("OTP expired for userId: {}", id);
                return ResponseEntity.status(400).body(new GetUserIdResponse(null, "OTP expired", 400));
            } else {
                logger.warn("Invalid OTP for userId: {}", id);
                return ResponseEntity.status(400).body(new GetUserIdResponse(null, "Invalid OTP", 400));
            }
        } else {
            logger.warn("No OTP found for userId: {}", id);
            return ResponseEntity.status(404).body(new GetUserIdResponse(null, "No OTP found", 404));
        }
    }


    @Operation(summary = "Get All Categories", description = "Retrieve all product categories.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Total Category Found : 12",
                            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Category Found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    @GetMapping("getallcategory")
    public ResponseEntity<CategoryResponse> getallcategory() {


        List<Category> category = healthCheckService.getallcategory();


        if (category.isEmpty()) {
            return ResponseEntity.status(204).body(new CategoryResponse(204, "No Category Found", category));
        }

        return ResponseEntity.status(200).body(new CategoryResponse(200, "Total Category Found : " + category.size(), category));
    }



    @Operation(summary = "Get All Articles", description = "Retrieve all Articles ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Total Article Found : 12",
                            content = @Content(schema = @Schema(implementation = ArticleResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Article Found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    @GetMapping("getAllArticle")
    public ResponseEntity<ArticleResponse> getAllArticle() {


        List<Article> articles = healthCheckService.getAllArticle();


        if (articles.isEmpty()) {
            return ResponseEntity.status(204).body(new ArticleResponse(204, "No Article Found", articles));
        }

        return ResponseEntity.status(200).body(new ArticleResponse(200, "Total Article Found : " + articles.size(), articles));
    }


    @Operation(summary = "Analyze Product Chemicals", description = "Analyze the chemicals in a product.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Fetch  Product Chemical Successfully",
                            content = @Content(schema = @Schema(implementation = ProductChemicalResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No result found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }


    )
    @PostMapping("/get-product-chemicals")
    public ResponseEntity<?> generateResponse(@RequestBody String productText) {
        ProductChemicalResponseDTO responseDTO = openAIService.fetchAllChemicals(productText);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("No result found");
        }

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Evaluate Product",
            description = "Evaluate a product based on user details and ingredients.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product Evaluated Successfully",
                            content = @Content(schema = @Schema(implementation = ProductEvaluationDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No result found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ProductEvaluationRequest.class))
            )

    )
    @PostMapping("/evaluate-product")
    public ResponseEntity<?> evaluateProduct(@RequestBody ProductEvaluationRequest request) {
        ProductEvaluationDTO responseDTO = openAIService.fetchProductEvaluation(request);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("No result found");
        }

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Get Product Usage Guide",
            description = "Provides usage guidelines based on product evaluation data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usage guidelines provided",
                            content = @Content(schema = @Schema(implementation = UsageGuideDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No guidelines found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ProductEvaluationRequest.class))
            )
    )
    @PostMapping("/get-product-guide")
    public ResponseEntity<?> productGuide(@RequestBody ProductEvaluationRequest request) {
        UsageGuideDTO responseDTO = openAIService.fetchProductUsageGuide(request);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("No result found");
        }

        return ResponseEntity.ok(responseDTO);
    }

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
    @PostMapping("/get-good-chemical-table")
    public ResponseEntity<?> goodChemicalTable(@RequestBody GoodChemicalRequestDTO goodChemicalRequestDTO){

        GoodChemicalResponseDTO responseDTO = openAIService.fetchGoodChemicalTable(goodChemicalRequestDTO);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("No result found");
        }

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Get Harmful Chemical Usage Table Data",
            description = "Provides Harmful Chemical Usage Table Data  based on good chemical evaluation data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Harmful Chemical Usage Table Data provided",
                            content = @Content(schema = @Schema(implementation = HarmfulChemicalResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No Harmful Chemical Usage Table Data found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = HarmfulChemicalRequestDTO.class))
            )
    )
    @PostMapping("/get-harmful-chemical-table")
    public ResponseEntity<?> harmfulChemicalTable(@RequestBody HarmfulChemicalRequestDTO harmfulChemicalRequestDTO){

        HarmfulChemicalResponseDTO responseDTO = openAIService.fetchHarmfulChemicalTable(harmfulChemicalRequestDTO);

        if (responseDTO == null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("No result found");
        }

        return ResponseEntity.ok(responseDTO);
    }


}
