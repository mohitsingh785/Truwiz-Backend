package org.Jtech.Service;


import jakarta.transaction.Transactional;
import org.Jtech.Constant.OtpPurpose;
import org.Jtech.DTO.*;
import org.Jtech.Entity.*;
import org.Jtech.Exception.AllergyNotFoundException;
import org.Jtech.Exception.InvalidCredentialsException;
import org.Jtech.Exception.UserAlreadyExistsException;
import org.Jtech.Exception.UserNotFoundException;
import org.Jtech.Model.OtpResponse;
//import org.Jtech.Repository.EmailOtpRepository;
import org.Jtech.Model.UserAndDetails;
import org.Jtech.Repository.*;
import org.Jtech.jwt.JwtHelper;
import org.Jtech.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


//    @Autowired
//    private EmailOtpRepository emailOtpRepository;


    // Update the user's password using userId (expects hashed password)
    public boolean changeUserPassword(Long userId, String newPassword) {
        // Assuming newPassword is already hashed if needed before this method is called
        int affectedRows = userRepository.updatePassword(userId, newPassword);
        // Return true if one or more rows were updated
        return affectedRows > 0;
    }

    // This method is used to fetch the userId using the Email (12/18/2025)
    public Long getUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

//    // This method is used to fetch user details by using the Email (12/18/2025)
//    public Optional<UserData> fetchUserByEmailPassword(String email) {
//        return userRepository.findByEmailAndPassword(email);
//    }

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

//    // Retrieve OTP and creation timestamp for email verification during signup
//    public Optional<OtpResponse> getOtpAndEmailVerify(Integer userId) {
//        return emailOtpRepository.findOtpAndCreatedAtByUserId(userId);
//    }


}
