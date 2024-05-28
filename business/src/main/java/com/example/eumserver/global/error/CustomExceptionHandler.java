package com.example.eumserver.global.error;

import com.example.eumserver.domain.jwt.JwtTokenInvalidException;
import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.global.dto.ApiResult;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Controller단과 Filter단에서 발생하는 모든 Exception을 Handling합니다.
 * @author Sukju Hong
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResult<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResult<>(errorCode));
    }

    /**
     * 인가 과정에 대한 ExceptionHandler
     * {@link DelegatingAccessDeniedHandler}에서
     * {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어옵니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResult<?>> handleAccessDeniedHandler(final AccessDeniedException e) {
        final ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResult<>(errorCode));
    }

    /**
     * {@link jakarta.validation.Valid} annotaion에 의해 validation이 실패했을경우
     * Controller 단에서 발생하여 Error가 넘어옵니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResult<>(errorCode));
    }

    /**
     * 인증 과정에 대한 ExceptionHandler
     * {@link JwtTokenProvider#validateToken(String)}에서 발생한 오류가 try-catch되어
     * {@link jakarta.servlet.http.HttpServletRequest#setAttribute(String, Object)}로 Exception이 bingding되고,
     * 해당 Exception이 {@link DelegatingAuthenticationEntryPoint}에서 받아져 {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어옵니다.
     */
    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<ApiResult<?>> handlerTokenNotValidateException(final JwtTokenInvalidException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResult<>(errorCode));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResult<?>> handleExpectedException(final CustomException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResult<>(errorCode));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResult<?>> handle404(NoHandlerFoundException e){
        final ErrorCode errorCode = ErrorCode.PAGE_NOT_FOUND;
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ApiResult<>(errorCode));
    }

    /**
     * 처리되지 않은 오류가 여기에서 모두 잡힙니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<?>> handleUnExpectedException(final Exception e) {
        log.error("unhandled exception: {}", e.getMessage(), e);
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ApiResult<>(errorCode));
    }


}
