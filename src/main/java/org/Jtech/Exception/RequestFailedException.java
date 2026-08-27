package org.Jtech.Exception;

public class RequestFailedException extends RuntimeException{

    private final String email;
    public RequestFailedException(String message,String email){
        super(message);
        this.email=email;
    }

    public String getEmail() {
        return email;
    }
}
