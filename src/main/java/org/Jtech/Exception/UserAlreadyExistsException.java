package org.Jtech.Exception;

public class UserAlreadyExistsException  extends RuntimeException{
    private final String email;
    public UserAlreadyExistsException(String message,String email){
        super(message);
        this.email=email;
    }

    public String getEmail() {
        return email;
    }
}
