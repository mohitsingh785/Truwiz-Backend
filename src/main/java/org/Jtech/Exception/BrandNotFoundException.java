package org.Jtech.Exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long id) {
        super("Brand not found with given brandId"+id);
    }
}
