package org.Jtech.Exception;

public class DuplicateBrandException extends RuntimeException {
    public DuplicateBrandException(String brandName) {
        super("Brand with name '" + brandName + "' already exists");
    }
}
