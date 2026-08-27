package org.Jtech.Exception;

public class AllergyNotFoundException extends RuntimeException{
    private final String email;
    public AllergyNotFoundException(String message,String email){
        super(message);
        this.email=email;
    }

    public String getEmail() {
        return email;
    }
}
