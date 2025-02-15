package org.Jtech.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordUtil {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
}
