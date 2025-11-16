package com.dlsoftware.msvc.items_msvc.commons.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;
    private String code;
    private String message ;

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}