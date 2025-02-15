package org.Jtech.Model;



import org.Jtech.Entity.User;
import org.Jtech.Entity.UserDetails;

public class UserAndDetails {
    private User user;
    private UserDetails userDetails;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
