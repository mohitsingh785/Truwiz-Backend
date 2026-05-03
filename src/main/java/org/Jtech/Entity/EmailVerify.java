package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.sql.Timestamp;

// Deprecated: Replaced by otp_verify (03-May-2026, Mohit Singh)
/*
@Entity
@Table(name = "Email_otp")
public class EmailVerify {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    @JsonProperty("otp_id")
    Integer id;


    @Column(name = "Otp", nullable = false)
    @JsonProperty("Otp") // Map JSON "Otp" to Java "otp"
    private String otp;

    @Column(name = "created_at", insertable = true, updatable = true)
    private Timestamp createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
 */
