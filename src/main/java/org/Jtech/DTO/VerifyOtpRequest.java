package org.Jtech.DTO;

import org.Jtech.Constant.OtpPurpose;
import org.springframework.web.bind.annotation.RequestParam;

public class VerifyOtpRequest {
    private String otp;
    private String email;
    private OtpPurpose otpPurpose;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OtpPurpose getOtpPurpose() {
        return otpPurpose;
    }

    public void setOtpPurpose(OtpPurpose otpPurpose) {
        this.otpPurpose = otpPurpose;
    }
}
