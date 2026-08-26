package org.Jtech.Exception;

public class OtpAlreadyUsedException extends RuntimeException{
    public OtpAlreadyUsedException(String message){
        super(message);
    }
}
