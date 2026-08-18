package org.Jtech.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    private boolean success;
    private Long userId;
    private String token;
    @JsonProperty("user_details")
    private CombinedUserDetails combinedUserDetails;

    public LoginResponse(boolean success, Long userId, String token, CombinedUserDetails combinedUserDetails) {
        this.success = success;
        this.userId = userId;
        this.token = token;
        this.combinedUserDetails = combinedUserDetails;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CombinedUserDetails getCombinedUserDetails() {
        return combinedUserDetails;
    }

    public void setCombinedUserDetails(CombinedUserDetails combinedUserDetails) {
        this.combinedUserDetails = combinedUserDetails;
    }
}
