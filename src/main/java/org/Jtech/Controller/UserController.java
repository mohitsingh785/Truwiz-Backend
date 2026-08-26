package org.Jtech.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * User Controller
 *
 * Purpose:
 * Manages user profile and account-related information such as
 * personal details, preferences, and application-specific settings.
 *
 * Scope:
 * - Fetch user profile information
 * - Update user details
 * - Manage user preferences (e.g., allergies, skin/scalp type)
 *
 * Metadata:
 * Created on : 2025-12-29
 * Author     : Mohit Singh
 *
 * Notes:
 * Authentication and password management are intentionally
 * handled outside this controller to maintain separation of concerns.
 */


@RestController
@RequestMapping("/v1/users")
@Tag(
        name="Users",
        description = "User profile, preference, and personal information management"
)
public class UserController {

    @Autowired
    private UserService userService;



}
