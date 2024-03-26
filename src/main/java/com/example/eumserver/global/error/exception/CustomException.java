package com.example.eumserver.global.error.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final int status;

    public CustomException(int status, String msg) {
        super(msg);
        this.status = status;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
