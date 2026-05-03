package org.Jtech.Model;

public class GetUserIdResponse {



    private String email;
    private String message;
    private int responseCode;

    public GetUserIdResponse(String email,String message,int responseCode){


        this.email=email;
        this.message=message;
        this.responseCode=responseCode;

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

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
