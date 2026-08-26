package org.Jtech.Exception;

public class RequestFailedException extends RuntimeException{
    public RequestFailedException(String message){
        super(message);
    }
}
