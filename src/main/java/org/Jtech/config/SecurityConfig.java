package org.Jtech.config;

import org.Jtech.jwt.JwtAuthenticationEntryPoint;
import org.Jtech.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration
 *
 * Purpose:
 * Configures application-level security using Spring Security,
 * including JWT-based authentication and authorization rules.
 *
 * Scope:
 * - Defines security filter chain
 * - Configures public and protected endpoints
 * - Integrates JWT authentication filter
 * - Configures authentication provider
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This configuration enforces stateless authentication using JWT.
 * CSRF is disabled as the application does not rely on session-based authentication.
 */


@Configuration
public class SecurityConfig {


    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Configure the security filter chain.
     *
     * Defines:
     * - Public (unauthenticated) endpoints
     * - JWT-based request filtering
     * - Stateless session management
     *
     * @param http HttpSecurity configuration
     * @return configured SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests().
                requestMatchers("/v1/auth/login", "/v1/auth//password/reset" ,"/v1/auth/signup",  "/v1/auth/otp/password-reset", "/v1/auth/otp/email-verification","/v1/auth/otp/verify-reset","/v1/auth/otp/verify-signup","/terms").permitAll() // Allow without authentication
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/configuration/ui","/swagger-resources/**","/swagger-ui.html", "/configuration/**", "/swagger-ui.html", "/webjars/**").permitAll() // Allow Swagger UI access
                .anyRequest()
                .authenticated()
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configure DAO-based authentication provider.
     *
     * Uses:
     * - Custom UserDetailsService
     * - BCrypt password encoder
     *
     * @return configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }


}