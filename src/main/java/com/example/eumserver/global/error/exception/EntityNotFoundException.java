package com.example.eumserver.global.error.exception;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String msg) {
        super(400, msg);
    }

}
