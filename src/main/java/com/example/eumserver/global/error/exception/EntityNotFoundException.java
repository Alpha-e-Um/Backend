package com.example.eumserver.global.error.exception;

import com.example.eumserver.global.error.CustomException;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String msg) {
        super(400, msg);
    }

}
