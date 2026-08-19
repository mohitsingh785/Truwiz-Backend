package org.Jtech.Model;

public class GetOtpResponse {


    private boolean status;
    private String email;
    private String message;

    public GetOtpResponse(boolean status, String email, String message) {
        this.status = status;
        this.email = email;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
