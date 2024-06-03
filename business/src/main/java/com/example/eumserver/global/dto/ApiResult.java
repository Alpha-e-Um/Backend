package com.example.eumserver.global.dto;

import com.example.eumserver.global.error.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResult<T> {

    private final boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    public ApiResult(String message, ErrorCode error) {
        this.success = false;
        this.code = error.getCode();
        this.message = error.getMessage() + " => " + message;
    }

}
