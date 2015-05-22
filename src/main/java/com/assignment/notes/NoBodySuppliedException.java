package com.assignment.notes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No Body Supplied")
public class NoBodySuppliedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public NoBodySuppliedException() {
        super("No Body Supplied");
    }
}
