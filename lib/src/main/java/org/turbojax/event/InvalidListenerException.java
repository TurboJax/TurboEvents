package org.turbojax.event;

public class InvalidListenerException extends RuntimeException {
    public InvalidListenerException(String message) {
        super(message);
    }
}