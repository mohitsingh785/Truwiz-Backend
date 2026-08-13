package org.Jtech.Service;

import org.Jtech.DTO.UserData;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.DTO.UserFullData;
import org.Jtech.Entity.User;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User Service
 *
 * Purpose:
 * Handles user profile and account-related business logic,
 * including retrieval of personal details and integration
 * with Spring Security for authentication context loading.
 *
 * Scope:
 * - Fetch user profile and personal details
 * - Aggregate core and extended user profile data
 * - Load user information for JWT-based authentication
 *
 * Metadata:
 * Created on : 2025-12-29
 * Author     : Mohit Singh
 *
 * Notes:
 * Authentication operations such as login and password
 * validation are handled outside this service to maintain
 * separation of concerns.
 */



@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository user;

    @Autowired
    private UserDetailsRepository userDetails;



    /**
     * Retrieve full user profile data using user ID.
     *
     * Used for:
     * - Aggregating core user data with extended profile details
     * - Internal testing and profile retrieval scenarios
     *
     * @param id unique identifier of the user
     * @return aggregated user profile data
     */
    /*
    public UserFullData printUserDetails(Long id){

        UserData userdata=user.alldata(id);
        UserDetailsDTO userDetailsDTO=userDetails.userDetailsData(id);

        // Construct and return UserFullData
        return new UserFullData(
                userdata.getUserName(),
                userdata.getEmail(),
                userdata.getPhoneNumber(),
                userDetailsDTO.getSkinType(),
                userDetailsDTO.getAge(),
                userDetailsDTO.getGender(),
                userDetailsDTO.getSkinColour(),
                userDetailsDTO.getAllergies(),
                userDetailsDTO.getHeightCm(),
                userDetailsDTO.getWeightKg()
        );

    }
/*

    /**
     * Retrieve full user profile data using user ID.
     *
     * Used for:
     * - Aggregating core user data with extended profile details
     * - Internal testing and profile retrieval scenarios
     *
     * @param userId unique identifier of the user
     * @return aggregated user profile data
     */
    /*
    public UserDetailsDTO getUserById(Long userId){
        // Fetch user details from the repository
        return  userDetails.userDetailsData(userId);
    }

    /*
    /**
     * Load user details by username (email).
     *
     * Used by:
     * - Spring Security during JWT authentication
     *
     * @param username user's email address
     * @return authenticated user details
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return  user.findByEmail(username).orElseThrow(()-> new RuntimeException("User Not Found"));
    }

}
