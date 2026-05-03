package org.Jtech.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.Jtech.Constant.OtpPurpose;

import java.sql.Timestamp;


/**
 * OTP Entity
 *
 * Purpose:
 * Represents the OTP (One-Time Password) verification data
 * registration, and password reset.
 *
 * Metadata:
 * Created on :
 * Author     : Mohit Singh
 *
 * Modification:
 * Modified the Entity with new fields (03-May-2026, Mohit Singh)
 */

@Entity
@Table(name = "otp_verify")
public class OTP {

    @Id
    @Column(name = "otp_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer otpId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "otp_hash", nullable = false)
    private String otpHash;

    @Column(name = "created_at", insertable = true)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = true,updatable = true)
    private Timestamp updatedAt;

    @Column(name = "expiry_time", insertable = true,updatable = true)
    private Timestamp expiryTime;

    @Column(name="is_used",insertable = true,updatable = true)
    private boolean used;

    @Column(name ="purpose", nullable = false)
    @Enumerated(EnumType.STRING)
    private OtpPurpose otpPurpose;

    public Integer getOtpId() {
        return otpId;
    }

    public void setOtpId(Integer otpId) {
        this.otpId = otpId;
    }

    public OtpPurpose getOtpPurpose() {
        return otpPurpose;
    }

    public void setOtpPurpose(OtpPurpose otpPurpose) {
        this.otpPurpose = otpPurpose;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Timestamp expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
