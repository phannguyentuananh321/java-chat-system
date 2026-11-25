package edu.hcmus.chat.admin.service;

/**
 * Thrown by the service layer when an action can't be done. The message is
 * already user-facing Vietnamese text, so the UI can show it as it is.
 * The UI never sees an SQLException directly.
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
