package org.Jtech.Service;


import jakarta.transaction.Transactional;
import org.Jtech.Constant.OtpPurpose;
import org.Jtech.DTO.UserData;
import org.Jtech.Entity.OTP;
import org.Jtech.Model.OtpResponse;
import org.Jtech.Repository.EmailOtpRepository;
import org.Jtech.Repository.OtpRepository;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Auth Service
 *
 * Purpose:
 * Provides authentication-related business logic including
 * user credential validation and password management.
 *
 * Scope:
 * - Authenticate users using email credentials
 * - Retrieve user identity information
 * - Handle password change operations
 *
 * Metadata:
 * Created on : 2025-12-29
 * Author     : Mohit Singh
 *
 * Notes:
 * This service contains only authentication business logic.
 * HTTP concerns are handled in the AuthController layer.
 */

@Service
public class AuthService {


    @Autowired
    private UserRepository user;

    @Autowired
    private UserDetailsRepository userDetails;

    @Autowired
    private OtpRepository otpRepository;


    @Autowired
    private EmailOtpRepository emailOtpRepository;

    // Fetch user authentication data using email
    public Optional<UserData> authenticate(String email) {
        return user.findByEmailAndPassword(email);
    }

    // Update the user's password using userId (expects hashed password)
    public boolean changeUserPassword(Long userId, String newPassword) {
        // Assuming newPassword is already hashed if needed before this method is called
        int affectedRows = user.updatePassword(userId, newPassword);
        // Return true if one or more rows were updated
        return affectedRows > 0;
    }

    // This method is used to fetch the userId using the Email (12/18/2025)
    public Long getUserIdByEmail(String email){
        return user.findUserIdByEmail(email);
    }

    // This method is used to fetch user details by using the Email (12/18/2025)
    public Optional<UserData> fetchUserByEmailPassword(String email) {
        return user.findByEmailAndPassword(email);
    }

    // Persist or update OTP for password reset flow
    @Transactional
    public void saveOrUpdateOtp(OTP otp) {
        otpRepository.save(otp);
    }

    // Retrieve OTP and creation timestamp for password reset verification
    public Optional<OTP> getOtpAndCreatedAtByEmail(String email, OtpPurpose otpPurpose) {
        return otpRepository.findTopByEmailAndOtpPurposeOrderByUpdatedAtDesc(email,otpPurpose);
    }

//    // Retrieve OTP and creation timestamp for email verification during signup
//    public Optional<OtpResponse> getOtpAndEmailVerify(Integer userId) {
//        return emailOtpRepository.findOtpAndCreatedAtByUserId(userId);
//    }


}
