package org.Jtech.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "otp_verify")
public class OTP {

    @Id
    @Column(name = "user_id", nullable = false)
    @JsonProperty("user_id") // Map JSON "user_id" to Java "userId"
    private Integer userId;

    @Column(name = "Otp", nullable = false)
    @JsonProperty("Otp") // Map JSON "Otp" to Java "otp"
    private String otp;

    @Column(name = "created_at", insertable = true, updatable = true)
    private Timestamp createdAt;

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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
