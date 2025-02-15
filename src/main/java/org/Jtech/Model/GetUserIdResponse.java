package org.Jtech.Model;

public class GetUserIdResponse {



    private Long id;
    private String message;
    private int responseCode;

    public GetUserIdResponse(Long id,String message,int responseCode){


        this.id=id;
        this.message=message;
        this.responseCode=responseCode;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
