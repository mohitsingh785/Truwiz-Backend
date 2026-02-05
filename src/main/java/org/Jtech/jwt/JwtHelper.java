package org.Jtech.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Helper
 *
 * Purpose:
 * Provides utility methods for generating, parsing, and validating
 * JSON Web Tokens (JWT) used for stateless authentication.
 *
 * Scope:
 * - Generate JWT tokens for authenticated users
 * - Extract claims such as username from tokens
 * - Validate tokens against user details
 *
 * Metadata:
 * Added on : 2026-02-06
 * Author   : Mohit Singh
 *
 * Notes:
 * This class contains security-sensitive logic.
 * Any changes should be reviewed carefully to avoid
 * authentication vulnerabilities.
 */


@Component
public class JwtHelper {



    //    public static final long JWT_TOKEN_VALIDITY =  60;
    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    /**
     * Extract the username (subject) from a JWT token.
     *
     * @param token JWT token
     * @return username stored in the token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    /**
     * Extract a specific claim from a JWT token.
     *
     * @param token JWT token
     * @param claimsResolver function to resolve a claim
     * @param <T> claim type
     * @return extracted claim value
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieve all claims from a JWT token using the signing key.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }



    /**
     * Generate a JWT token for an authenticated user.
     *
     * @param userDetails authenticated user details
     * @return generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    /**
     * Build and sign a JWT token.
     *
     * @param claims custom claims
     * @param subject token subject (username)
     * @return signed JWT token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Validate a JWT token against user details.
     *
     * @param token JWT token
     * @param userDetails authenticated user details
     * @return true if token is valid
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()));
    }


}