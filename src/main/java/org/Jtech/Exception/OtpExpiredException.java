package org.Jtech.Exception;

public class OtpExpiredException extends RuntimeException{
    public OtpExpiredException(String message){
        super(message);
    }
}
