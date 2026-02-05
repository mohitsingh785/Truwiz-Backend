package org.Jtech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Security Configuration
 *
 * Purpose:
 * Defines core security-related beans required for authentication
 * and password encryption within the application.
 *
 * Scope:
 * - Password encoding using BCrypt
 * - AuthenticationManager bean configuration
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This configuration supports Spring Security authentication
 * mechanisms and is used across the authentication flow.
 */


@Configuration
class MyConfig {

    /**
     * Provides a BCrypt-based password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager bean required by Spring Security.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}