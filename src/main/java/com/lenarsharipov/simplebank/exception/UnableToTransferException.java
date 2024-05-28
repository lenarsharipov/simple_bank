package com.lenarsharipov.simplebank.exception;

public class UnableToTransferException extends RuntimeException {

    public UnableToTransferException(String message) {
        super(message);
    }
}
