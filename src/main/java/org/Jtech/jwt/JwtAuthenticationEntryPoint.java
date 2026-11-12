package org.Jtech.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.time.Instant;

/**
 * JWT Authentication Entry Point
 *
 * Purpose:
 * Handles unauthorized access attempts to secured endpoints.
 *
 * Scope:
 * - Sends HTTP 401 (Unauthorized) responses
 * - Returns a meaningful error message when authentication fails
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This component is triggered when a request is made to a protected
 * resource without a valid JWT token.
 */


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Shared with JwtAuthenticationFilter: the filter writes, this class reads
    public static final String JWT_ERROR_ATTRIBUTE = "jwt_error";
    public static final String JWT_EXPIRED = "expired";
    public static final String JWT_INVALID = "invalid";

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Why is there no authenticated user? The filter leaves the reason here.
        Object jwtError = request.getAttribute(JWT_ERROR_ATTRIBUTE);

        String detail;
        if (JWT_EXPIRED.equals(jwtError)) {
            detail = "Token has expired. Please log in again.";
        } else if (JWT_INVALID.equals(jwtError)) {
            detail = "Invalid token.";
        } else {
            detail = "Authentication is required to access this resource.";
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail);
        problemDetail.setTitle("Unauthorized");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}