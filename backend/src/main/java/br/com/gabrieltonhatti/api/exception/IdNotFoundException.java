package br.com.gabrieltonhatti.api.exception;

import javassist.NotFoundException;

public class IdNotFoundException extends NotFoundException {

    public IdNotFoundException(String message) {
        super(message);
    }
    
}
