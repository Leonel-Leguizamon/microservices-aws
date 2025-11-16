package com.dlsoftware.msvc.products_msvc.commons.exceptions;

public class ProductNotFoundException extends BaseException{

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String code, String message) {
        super(code, message);
    }

}
