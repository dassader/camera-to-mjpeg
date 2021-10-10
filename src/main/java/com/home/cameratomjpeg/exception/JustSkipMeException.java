package com.home.cameratomjpeg.exception;

public class JustSkipMeException extends RuntimeException {
    public JustSkipMeException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        //Fast create
        return this;
    }
}
