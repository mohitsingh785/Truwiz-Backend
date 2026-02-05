package org.Jtech.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
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

    /**
     * Commences an authentication scheme.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param authException authentication exception
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("Access Denied !! " + authException.getMessage());
    }
}