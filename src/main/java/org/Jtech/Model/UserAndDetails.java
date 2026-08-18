package org.Jtech.Model;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.Jtech.DTO.UserDetailsDTO;
import org.Jtech.Entity.User;
import org.Jtech.Entity.UserDetails;

public class UserAndDetails {
    @Valid
    private User user;
    @Valid
    @JsonProperty("userDetails")
    private UserDetailsDTO userDetailsDTO;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetailsDTO getUserDetailsDTO() {
        return userDetailsDTO;
    }

    public void setUserDetailsDTO(UserDetailsDTO userDetailsDTO) {
        this.userDetailsDTO = userDetailsDTO;
    }
}
