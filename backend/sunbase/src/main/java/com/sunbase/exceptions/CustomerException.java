package com.sunbase.exceptions;

import lombok.Data;

import java.util.List;

@Data
public class CustomerException extends  RuntimeException{

    private String message;
    private List<Errors> errors;
    public CustomerException(String message) {
        super(message);
        this.message = message;
    }
}
