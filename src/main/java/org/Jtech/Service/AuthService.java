package org.Jtech.Service;


import jakarta.transaction.Transactional;
import org.Jtech.Constant.OtpPurpose;
import org.Jtech.DTO.*;
import org.Jtech.Entity.*;
import org.Jtech.Exception.*;
import org.Jtech.Model.GetOtpResponse;
import org.Jtech.Model.UserAndDetails;
import org.Jtech.Repository.*;
import org.Jtech.jwt.JwtHelper;
import org.Jtech.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import static org.Jtech.Constant.Const.OTP_EXPIRY_MILLIS;

/**
 * Auth Service
 * <p>
 * Purpose:
 * Provides authentication-related business logic including
 * user credential validation and password management.
 * <p>
 * Scope:
 * - Authenticate users using email credentials
 * - Retrieve user identity information
 * - Handle password change operations
 * <p>
 * Metadata:
 * Created on : 2025-12-29
 * Author     : Mohit Singh
 * <p>
 * Notes:
 * This service contains only authentication business logic.
 * HTTP concerns are handled in the AuthController layer.
 */

@Service
@Transactional
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private AllergiesRepository allergiesRepository;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private UserAllergyRepository userAllergyRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private EmailService emailService;



    // Update the user's password using userId (expects hashed password)
    public boolean changeUserPassword(Long userId, String newPassword) {
        // Assuming newPassword is already hashed if needed before this method is called
        int affectedRows = userRepository.updatePassword(userId, newPassword);
        // Return true if one or more rows were updated
        return affectedRows > 0;
    }

    // Persist or update OTP for password reset flow
    public void saveOrUpdateOtp(OTP otp) {
        otpRepository.save(otp);
    }

    // Retrieve OTP and creation timestamp for password reset verification
    public Optional<OTP> getOtpAndCreatedAtByEmail(String email, OtpPurpose otpPurpose) {
        return otpRepository.findTopByEmailAndOtpPurposeOrderByUpdatedAtDesc(email, otpPurpose);
    }


    // Register the User
    public void registerUser(UserAndDetails userAndDetails) {
        User user = userAndDetails.getUser();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User Already Exist with email " + user.getEmail());
        }
        String encryptPass = utilsService.hashPassword(userAndDetails.getUser().getPassword());
        user.setPassword(encryptPass);
        UserDetailsDTO userDetailsDto = userAndDetails.getUserDetailsDTO();

        UserDetails userDetails = userMapper.createUserDetails(userAndDetails.getUserDetailsDTO());

        userDetails.setUser(user);
        user.setUserDetails(userDetails);
        userRepository.save(user);

        List<Allergies> allergies =
                allergiesRepository.findAllById(userDetailsDto.getAllergyIds());

        if (allergies.size() != userDetailsDto.getAllergyIds().size()) {
            throw new AllergyNotFoundException("Invalid allergy IDs supplied.");
        }
        for (Allergies allergy : allergies) {
            UserAllergy userAllergy = new UserAllergy(userDetails, allergy);
            userAllergyRepository.save(userAllergy);
        }

    }

    // Authenticate the User
    public LoginResponse authenticate(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Verify if user exist
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));
        Long userId = user.getUserId();

        // Get the Encrypted password from user entity
        String encryptedPassword = user.getPassword();

        // Verify is entered password match with user password
        if (!utilsService.matchPassword(password, encryptedPassword)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        // generate auth token
        org.springframework.security.core.userdetails.UserDetails userDetails = userService.loadUserByUsername(email);
        String token = this.helper.generateToken(userDetails);

        // Get the user details using the userId
        UserDetailsView userDetailsView = userDetailsRepository.findByUserUserId(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        // Get the User Allergy using the user details id
        List<UserAllergy> userAllergies = userAllergyRepository.findByUserDetailsDetailsId(userDetailsView.getDetailsId());

        // Create the list to store user allergy
        List<String> storeUserAllergies = userAllergies.stream().
                map(UserAllergy::getAllergy).
                map(Allergies::getAllergyName).
                toList();

        // return final LoginResponse
        return userMapper.createLoginResponse(user, userDetailsView, storeUserAllergies, token);
    }


    // Reset the User Password
    public void passwordReset(PasswordResetRequest passwordResetRequest) {

        Long userId = passwordResetRequest.getId();
        String newPassword = passwordResetRequest.getNewPassword();
        String oldPassword = passwordResetRequest.getOldPassword();
        User user = userRepository.findByuserId(userId)
                .orElseThrow(() ->
                        new InvalidCredentialsException("User not found."));

        // Get the Encrypted password from user entity
        String encryptedPassword = user.getPassword();

        // Verify is entered old password match with user password
        if (!utilsService.matchPassword(oldPassword, encryptedPassword)) {
            throw new InvalidCredentialsException("Current password is incorrect.");
        }

        // hash the new password
        String encryptPass = utilsService.hashPassword(newPassword);

        // update the password
        boolean updated = changeUserPassword(userId, encryptPass);

        if (!updated) {
            throw new RequestFailedException("Request Failed for Password reset");
        }
    }


    // Generate the otp for password reset
    public GetOtpResponse generateOtpForPasswordReset(String email){

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return new GetOtpResponse(
                    true,
                    email,
                    "If an account exists with this email, an OTP has been sent."
            );
        }

        // Generate a new OTP
        String generatedOtp = utilsService.generateOtp();

        Optional<OTP> existingOtp = otpRepository.findTopByEmailOrderByUpdatedAtDesc(email);
        String hashOtp = utilsService.hashOtp(generatedOtp);

        OTP otp = existingOtp.orElse(new OTP());
        long now = System.currentTimeMillis();

        if (existingOtp.isEmpty()) {
            otp.setCreatedAt(new Timestamp(now));
        }

        otp.setUpdatedAt(new Timestamp(now));

        otp.setExpiryTime(new Timestamp(now + OTP_EXPIRY_MILLIS));
        otp.setOtpHash(hashOtp);
        otp.setEmail(email);
        otp.setUsed(false);
        otp.setOtpPurpose(OtpPurpose.RESET_PASSWORD);

        // Save the otp
        saveOrUpdateOtp(otp);

        String subject = "Your OTP Code";
        String body = "Dear user,\n\nYour OTP code is:" + generatedOtp + "\n\nRegards,\nToxi Scan Teams";

        try {
            emailService.sendOtpEmail(email, subject, body);

        } catch (Exception ex) {
            throw new EmailSendFailedException(
                    "Failed to send OTP email. Please try again later.");
        }

        return new GetOtpResponse(true,email,"Otp sent successfully");
    }

    // Generate the otp for email verification
    public GetOtpResponse generateOtpForEmailVerification(String email){

        if (userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException("User Already Exist with email " + email);
        }

        // Generate a new OTP
        String generatedOtp = utilsService.generateOtp();

        Optional<OTP> existingOtp = otpRepository.findTopByEmailOrderByUpdatedAtDesc(email);
        String hashOtp = utilsService.hashOtp(generatedOtp);

        OTP otp = existingOtp.orElse(new OTP());
        long now = System.currentTimeMillis();

        if (existingOtp.isEmpty()) {
            otp.setCreatedAt(new Timestamp(now));
        }

        otp.setExpiryTime(new Timestamp(now + 60_000));
        otp.setOtpHash(hashOtp);
        otp.setEmail(email);
        otp.setUsed(false);
        otp.setOtpPurpose(OtpPurpose.REGISTER);
        saveOrUpdateOtp(otp);

        String subject = "Your OTP Code";
        String body = "Dear user,\n\nYour OTP code is:" + generatedOtp + "\n\nRegards,\nToxi Scan Teams";

        try {
            emailService.sendOtpEmail(email, subject, body);

        } catch (Exception ex) {
            throw new EmailSendFailedException(
                    "Failed to send OTP email. Please try again later.");
        }

        return new GetOtpResponse(true,email,"Otp sent successfully");
    }


    // Verify Otp for password reset and email verification
    public GetOtpResponse verifyOtp(VerifyOtpRequest verifyOtpRequest){

        OtpPurpose purpose = verifyOtpRequest.getOtpPurpose();
        OTP otpEntity = getOtpAndCreatedAtByEmail(
                verifyOtpRequest.getEmail(),
                purpose
        ).orElseThrow(() ->
                new OtpNotFoundException("OTP not found. Please request a new OTP."));

        long now = System.currentTimeMillis();
        // 1. Already used
        if (otpEntity.isUsed()) {
            throw new OtpAlreadyUsedException(
                    "OTP has already been used.");
        }
        // 2. Expired
        if (now > otpEntity.getExpiryTime().getTime()) {
            throw new OtpExpiredException(
                    "OTP has expired.");
        }
        // 3. Match
        if (!utilsService.matchOtp(verifyOtpRequest.getOtp(), otpEntity.getOtpHash())) {
            throw new InvalidOtpException(
                    "Invalid OTP.");
        }
        // 4. Mark used
        otpEntity.setUsed(true);
        otpRepository.save(otpEntity);

        return new GetOtpResponse(false, verifyOtpRequest.getEmail(), "OTP verified successfully");

    }

}
