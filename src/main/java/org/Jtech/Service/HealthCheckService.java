package org.Jtech.Service;

import jakarta.transaction.Transactional;
import org.Jtech.DTO.UserData;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.DTO.UserFullData;
import org.Jtech.Entity.*;
import org.Jtech.Model.OtpResponse;
import org.Jtech.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class HealthCheckService implements UserDetailsService {


    @Autowired
    private UserRepository user;

    @Autowired
    private UserDetailsRepository userdetails;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailOtpRepository emailOtpRepository;


    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KeyRepository keyRepository;





    public UserFullData print(Long id){

      UserData userdata=user.alldata(id);
      UserDetailsDTO userDetailsDTO=userdetails.userdetaildata(id);

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

    public Optional<UserData> authenticate(String email) {
        return user.findByEmailAndPassword(email);
    }


   public Long getuserIdbyemail(String email){



        return user.findUserIdByEmail(email);
   }

    @Transactional
    public void saveOrUpdateOtp(OTP otp) {
        otpRepository.save(otp);
    }

   public String generateotp(){


        Random random=new Random();

        StringBuilder ss=new StringBuilder();



       for (int i = 0; i < 4; i++) { // Generate 4 random digits
           ss.append(random.nextInt(10)); // Random digit between 0 and 9
       }

        return ss.toString();
   }

    public Optional<OtpResponse> getOtpAndCreatedAtByUserId(Integer userId) {
        return otpRepository.findOtpAndCreatedAtByUserId(userId);
    }

    public Optional<OtpResponse> getOtpAndEmailVerify(Integer userId) {
        return emailOtpRepository.findOtpAndCreatedAtByUserId(userId);
    }

    public boolean changeUserPassword(Long userId, String newPassword) {
        // Assuming newPassword is already hashed if needed before this method is called
        int affectedRows = user.updatePassword(userId, newPassword);
        // Return true if one or more rows were updated
        return affectedRows > 0;
    }

    public List<Category> getallcategory(){

        return categoryRepository.getAllCategories();
    }



    public String getGptKey(String KeyName){

        return keyRepository.getApiKey(KeyName).getKeyVal();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user1=user.findByEmail(username).orElseThrow(()-> new RuntimeException("User Not Found"));
        return user1;
    }
}
