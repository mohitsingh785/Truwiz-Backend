package org.Jtech.Service;

import org.Jtech.DTO.UserData;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.DTO.UserFullData;
import org.Jtech.Repository.UserDetailsRepository;
import org.Jtech.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository user;

    @Autowired
    private UserDetailsRepository userDetails;



    // This method is used to print user details using only the user ID, for testing purposes (12/18/2025).
    public UserFullData printUserDetails(Long id){

        UserData userdata=user.alldata(id);
        UserDetailsDTO userDetailsDTO=userDetails.userdetaildata(id);

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
                userDetailsDTO.getBmi(),
                userDetailsDTO.getWeight()
        );

    }




}
