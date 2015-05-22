package com.assignment.notes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class to support some simple error reporting back to the client.
 * This exception occurs when a POST request comes in with no "body".
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No Body Supplied")
public class NoBodySuppliedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public NoBodySuppliedException() {
        super("No Body Supplied");
    }
}
