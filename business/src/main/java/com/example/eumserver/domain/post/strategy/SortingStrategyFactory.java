package com.example.eumserver.domain.post.strategy;

import com.example.eumserver.domain.post.PostSortingOption;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 글 목록 필터를 Strategy전략을 활용하여, Factory로 분배해줍니다.
 */
@Component
public class SortingStrategyFactory {

    public <T extends EntityPathBase<?>> SortingStrategy<T> getStrategy(
            PostSortingOption option, NumberPath<Long> viewsPath, DateTimePath<LocalDateTime> createdAtpath) {
        switch (option) {
            case LATEST:
                return new LatestSortingStrategy<>(createdAtpath);
            case VIEWS:
                return new ViewsSortingStrategy<>(viewsPath);
            case POPULAR:
                return new PopularSortingStrategy<>(viewsPath, createdAtpath);
            default:
                throw new CustomException("잘못된 글 목록 옵션", ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
