package ru.party.meeting.exception;

public class JwtAuthenticationException extends Exception {

    public JwtAuthenticationException() {
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
