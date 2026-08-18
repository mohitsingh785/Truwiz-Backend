package org.Jtech.mapper;


import org.Jtech.DTO.CombinedUserDetails;
import org.Jtech.DTO.LoginResponse;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.DTO.UserDetailsView;
import org.Jtech.Entity.User;
import org.Jtech.Entity.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {


    // Save the UserDetailsDto into the UserDetails Entity
    public UserDetails createUserDetails(UserDetailsDTO userDetailsDTO) {

        return new UserDetails(
                userDetailsDTO.getSkinType(),
                userDetailsDTO.getHairType(),
                userDetailsDTO.getAge(),
                userDetailsDTO.getGender(),
                userDetailsDTO.getSkinColour(),
                userDetailsDTO.getHeightCm(),
                userDetailsDTO.getWeightKg()
        );
    }


    // save the userDetails and User into Login response
    public LoginResponse createLoginResponse(User user, UserDetailsView userDetailsView, List<String> allergies,String token){
        CombinedUserDetails combinedUserDetails=new CombinedUserDetails(

                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                userDetailsView.getSkinType(),
                userDetailsView.getHairType(),
                userDetailsView.getAge(),
                user.getUserDetails().getGender(),
                userDetailsView.getSkinColour(),
                allergies,
                userDetailsView.getHeightCm(),
                userDetailsView.getWeightKg()
        );
        return new LoginResponse(true,user.getUserId(),token,combinedUserDetails);
    }
}
