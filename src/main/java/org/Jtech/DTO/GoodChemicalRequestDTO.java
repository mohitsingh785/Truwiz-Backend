package org.Jtech.DTO;

import org.Jtech.Model.UserDetailsModel;

import java.util.List;

public class GoodChemicalRequestDTO {

    private List<String> chemicals;
    private UserDetailsModel userDetails;


    public List<String> getChemicals() {
        return chemicals;
    }

    public void setChemicals(List<String> chemicals) {
        this.chemicals = chemicals;
    }

    public UserDetailsModel getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsModel userDetails) {
        this.userDetails = userDetails;
    }
}
