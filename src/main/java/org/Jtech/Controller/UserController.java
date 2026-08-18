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

    /**
     * Retrieve user profile details by user ID.
     *
     * Used for:
     * - Fetching personal and preference-related information of a user
     * - Displaying user profile data in the application
     *
     * @param id unique identifier of the user
     * @return user profile details
     */

    /*
    @Operation(
            summary = "Get user profile details",
            description = "Retrieve user profile and preference details using the user ID"
    )
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please provide a valid ID.");
        }

        // Fetch user details from the repository
        UserDetailsDTO userDetails = userService.getUserById(id);

        // Check if user details are null (invalid ID)
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found for ID: " + id);
        }

        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }
*/

}
