package org.Jtech.DTO;

// Login result: token + account info + the same profile as GET /v1/users/me/profile.
public record LoginResponse(
        boolean success,
        Long userId,
        String token,
        UserAccountInfo user,
        UserProfileResponse profile
) {
}