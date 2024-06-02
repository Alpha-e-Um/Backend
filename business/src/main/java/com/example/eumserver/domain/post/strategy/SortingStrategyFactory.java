package com.example.eumserver.domain.post.strategy;

import com.example.eumserver.domain.post.PostSortingOption;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                throw new IllegalArgumentException("Unknown sorting option: " + option);
        }
    }
}
