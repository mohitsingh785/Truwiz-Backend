package org.Jtech.DTO;

// Account fields shown after login. Never contains the password.
public record UserAccountInfo(
        String userName,
        String email,
        String phoneNumber
) {
}