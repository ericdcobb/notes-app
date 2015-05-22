package com.assignment.notes;

import java.util.Map;

/**
 * Convenience class for exchanging error information
 */
public class JsonError {

	private final static String ERROR = "error";
	private final static String MESSAGE = "message";
	private final static String TIMESTAMP = "timestamp";
	private final static String TRACE = "trace";

    public Integer status;
    public String error;
    public String message;
    public String timeStamp;
    public String trace;

    public JsonError(int status, Map<String, Object> errorAttributes) {
        this.status = status;
        if (errorAttributes != null) {
            this.error = (String) errorAttributes.get(ERROR);
            this.message = (String) errorAttributes.get(MESSAGE);
            this.timeStamp = errorAttributes.get(TIMESTAMP).toString();
            this.trace = (String) errorAttributes.get(TRACE);
        }
    }
}
