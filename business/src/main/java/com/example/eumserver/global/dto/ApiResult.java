package com.example.eumserver.global.dto;

import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResult<T> {

    private final boolean success;
    private final String message;
    private String code;
    private T data;

    public ApiResult(String message){
        this.success = true;
        this.message = message;
    }

    public ApiResult(String message, T data){
        this.success = true;
        this.message = message;
        this.data = data;
    }

    public ApiResult(ErrorCode error){
        this.success = false;
        this.code = error.getCode();
        this.message = error.getMessage();
    }

}
