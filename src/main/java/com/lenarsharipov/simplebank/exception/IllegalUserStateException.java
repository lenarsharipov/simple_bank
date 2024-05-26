package com.lenarsharipov.simplebank.exception;

public class IllegalUserStateException extends RuntimeException {

    public IllegalUserStateException(String message) {
        super(message);
    }
}
