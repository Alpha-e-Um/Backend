package com.example.eumserver.global.interceptor;

import com.example.eumserver.global.annotation.MaxFileSize;
import com.example.eumserver.global.dto.ApiResult;
import com.example.eumserver.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

public class MaxFileSizeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            MaxFileSize annotation = handlerMethod.getMethodAnnotation(MaxFileSize.class);
            if (annotation != null) {
                long maxSize = annotation.value();
                MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file"); // 파일 파라미터 이름에 맞게 수정
                if (file != null && file.getSize() > maxSize) {
                    ErrorCode errorCode = ErrorCode.PAYLOAD_TOO_LARGE;
                    response.setHeader("Content-Type", "application/json;charset=utf-8");
                    response.setStatus(errorCode.getStatus());

                    ApiResult<?> apiResult = new ApiResult<>(errorCode);
                    String json = new ObjectMapper().writeValueAsString(apiResult);
                    response.getWriter().write(json);
                    return false;
                }
            }
        }
        return true;
    }

}
