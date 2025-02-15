package org.Jtech.Model;



import java.sql.Timestamp;

public class OtpResponse {
    private String otp;
    private Timestamp createdAt;

    // Constructor
    public OtpResponse(String otp, Timestamp createdAt) {
        this.otp = otp;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
