package org.Jtech.Model;



import java.sql.Timestamp;

public class OtpResponse {
    private String otpHash;
    private Timestamp expiryTime;
    private boolean used;

    // Constructor
    public OtpResponse(String otpHash, Timestamp expiryTime,boolean used) {
        this.otpHash = otpHash;
        this.expiryTime = expiryTime;
        this.used=used;
    }

    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Timestamp expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }
}
