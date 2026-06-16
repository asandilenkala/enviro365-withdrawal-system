package com.enviro.assessment.junior.asandile.exception;

/**
 * Exception thrown when a user tries to access a resource they don't have permission for
 *
 * @author Asandile
 * @version 1.0
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}