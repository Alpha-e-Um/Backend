package com.example.eumserver.global.error;

import com.example.eumserver.domain.jwt.JwtTokenInvalidException;
import com.example.eumserver.domain.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
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

    /**
     * 인가 과정에 대한 ExceptionHandler
     * {@link DelegatingAccessDeniedHandler}에서
     * {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어옵니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedHandler(final AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 인증 과정에 대한 ExceptionHandler
     * {@link JwtTokenProvider#validateToken(String)}에서 발생한 오류가 try-catch되어
     * {@link jakarta.servlet.http.HttpServletRequest#setAttribute(String, Object)}로 Exception이 bingding되고,
     * 해당 Exception이 {@link DelegatingAuthenticationEntryPoint}에서 받아져 {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어옵니다.
     */
    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<ErrorResponse> handlerTokenNotValidateException(final JwtTokenInvalidException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleExpectedException(final CustomException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getStatus())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException e){
        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponse.builder()
                        .status(e.getStatusCode().value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 처리되지 않은 오류가 여기에서 모두 잡힙니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnExpectedException(final Exception e) {
        log.error("unhandled exception: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build());
    }


}
