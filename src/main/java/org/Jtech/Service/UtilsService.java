package org.Jtech.Service;


import org.Jtech.Repository.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Utility Service
 *
 * Purpose:
 * Provides common utility functions used across the application,
 * including OTP generation and secure retrieval of configuration
 * or integration keys.
 *
 * Scope:
 * - Generate one-time passwords (OTP)
 * - Retrieve application-level keys for internal integrations
 *
 * Metadata:
 * Added on : 2026-02-01
 * Author   : Mohit Singh
 *
 * Notes:
 * This service contains helper methods only.
 * Sensitive values returned by this service must never be logged
 * or exposed through public APIs.
 */


@Service
public class UtilsService {

    @Autowired
    private KeyRepository keyRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    /**
     * Generate a 4-digit numeric one-time password (OTP).
     *
     * Used for:
     * - User verification
     * - Authentication-related flows
     *
     * @return generated 4-digit OTP as a string
     */
    public String generateOtp(){

        Random random=new Random();

        StringBuilder ss=new StringBuilder();

        for (int i = 0; i < 4; i++) { // Generate 4 random digits
            ss.append(random.nextInt(10)); // Random digit between 0 and 9
        }

        return ss.toString();
    }

    /**
     * Retrieve an internal API key by name.
     *
     * @param KeyName identifier of the required key
     * @return API key value for internal use
     *
     * Note:
     * This method is intended for internal service usage only.
     * Returned keys must not be logged, cached insecurely,
     * or exposed outside trusted layers.
     */
    public String getGptKey(String KeyName){

        return keyRepository.getApiKey(KeyName).getKeyVal();
    }

    /**
     * Hash the plain password using BCrypt.
     *
     * @param plainPassword the plain password to be hashed
     * @return the hashed password
     */
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Verify if the plain password matches the hashed password.
     *
     * @param plainPassword the plain password to verify
     * @param hashedPassword the hashed password to match against
     * @return true if passwords match, false otherwise
     */
    public boolean matchPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }

    /**
     * Hash the OTP using BCrypt.
     *
     * @param plainOtp the plain Otp to be hashed
     * @return the hashed Otp
     */
    public String hashOtp(String plainOtp) {
        return encoder.encode(plainOtp);
    }

    /**
     * Verify if the plain Otp matches the hashed Otp.
     *
     * @param plainOtp the plain Otp to verify
     * @param hashedOtp the hashed Otp to match against
     * @return true if Otp match, false otherwise
     */
    public boolean matchOtp(String plainOtp, String hashedOtp) {
        return encoder.matches(plainOtp, hashedOtp);
    }


}
