package com.assignment.notes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No Such Note")
public class NoteNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String ID_MESSAGE = "No such note (id: %d)";
    private static final String BODY_MESSAGE = "No such note (body: %s)";
    
    
    public NoteNotFoundException(Long id) {
        super(String.format(ID_MESSAGE, id));
    }
    
    public NoteNotFoundException(String body) {
        super(String.format(BODY_MESSAGE, body));
    }
}
