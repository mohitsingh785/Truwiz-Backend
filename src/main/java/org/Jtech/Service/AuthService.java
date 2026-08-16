package org.Jtech.Service;


import jakarta.transaction.Transactional;
import org.Jtech.Constant.OtpPurpose;
import org.Jtech.DTO.UserData;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.Entity.*;
import org.Jtech.Exception.AllergyNotFoundException;
import org.Jtech.Exception.UserAlreadyExistsException;
import org.Jtech.Model.OtpResponse;
//import org.Jtech.Repository.EmailOtpRepository;
import org.Jtech.Model.UserAndDetails;
import org.Jtech.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


//    @Autowired
//    private EmailOtpRepository emailOtpRepository;

    // Fetch user authentication data using email
    public Optional<UserData> authenticate(String email) {
        return userRepository.findByEmailAndPassword(email);
    }

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

    // This method is used to fetch user details by using the Email (12/18/2025)
    public Optional<UserData> fetchUserByEmailPassword(String email) {
        return userRepository.findByEmailAndPassword(email);
    }

    // Persist or update OTP for password reset flow
    @Transactional
    public void saveOrUpdateOtp(OTP otp) {
        otpRepository.save(otp);
    }

    // Retrieve OTP and creation timestamp for password reset verification
    public Optional<OTP> getOtpAndCreatedAtByEmail(String email, OtpPurpose otpPurpose) {
        return otpRepository.findTopByEmailAndOtpPurposeOrderByUpdatedAtDesc(email, otpPurpose);
    }


    // Save the UserDetailsDto into the UserDetails Entity
    public UserDetails createUserDetails(UserDetailsDTO userDetailsDTO) {

        return new UserDetails(
                userDetailsDTO.getSkinType(),
                userDetailsDTO.getHairType(),
                userDetailsDTO.getAge(),
                userDetailsDTO.getGender(),
                userDetailsDTO.getSkinColour(),
                userDetailsDTO.getHeightCm(),
                userDetailsDTO.getWeightKg()
        );
    }

    // Register the User
    public void registerUser(UserAndDetails userAndDetails)  {
        User user = userAndDetails.getUser();
        if(userRepository.existsByEmail(user.getEmail())){
         throw new UserAlreadyExistsException("User Already Exist with email "+user.getEmail());
        }
        String encryptPass = utilsService.hashPassword(userAndDetails.getUser().getPassword());
        user.setPassword(encryptPass);
        UserDetailsDTO userDetailsDto = userAndDetails.getUserDetailsDTO();

        UserDetails userDetails = createUserDetails(userAndDetails.getUserDetailsDTO());

        userDetails.setUser(user);
        user.setUserDetails(userDetails);
        userRepository.save(user);

        List<Allergies> allergies =
                allergiesRepository.findAllById(userDetailsDto.getAllergyIds());

        if (allergies.size()!=userDetailsDto.getAllergyIds().size()){
            throw new AllergyNotFoundException("Invalid allergy IDs supplied.");
        }
        for (Allergies allergy: allergies){
            UserAllergy userAllergy = new UserAllergy(userDetails, allergy);
            userAllergyRepository.save(userAllergy);
        }

    }

//    // Retrieve OTP and creation timestamp for email verification during signup
//    public Optional<OtpResponse> getOtpAndEmailVerify(Integer userId) {
//        return emailOtpRepository.findOtpAndCreatedAtByUserId(userId);
//    }


}
