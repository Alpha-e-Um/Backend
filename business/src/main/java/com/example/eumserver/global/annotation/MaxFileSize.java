package com.example.eumserver.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 업로드되는 파일의 최대 용량(bytes)을 설정합니다.
 * RequestPart의 이름을 file로 설정해야 적용됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {

    /**
     * 바이트 단위의 파일 최대 용량
     */
    long value();
}
