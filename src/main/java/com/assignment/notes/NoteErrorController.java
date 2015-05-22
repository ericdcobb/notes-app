package com.assignment.notes;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Override default Spring error handling by implementing ErrorController.
 */
@RestController
public class NoteErrorController implements ErrorController {

    private static final String PATH = "/error";
    private static final Boolean DEBUG = false;

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public JsonError error(HttpServletRequest request, HttpServletResponse response) {
        // Appropriate HTTP response code (e.g. 404 or 500) is automatically set by Spring.
        return new JsonError(response.getStatus(), getErrorAttributes(request, DEBUG));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    /**
     * Extract error attributes from the request.
     * 
     * @param request
     * @param includeStackTrace
     * @return Map to be passed into JsonError
     */
    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable exception = errorAttributes.getError(requestAttributes);
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
        if (attributes != null && exception != null) {
            // replace message with one from exception
            attributes.put("message", exception.getMessage());
        }
        return attributes;
    }
}
