package com.example.eumserver.domain.post.strategy;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class LatestSortingStrategy<T extends EntityPathBase<?>> implements SortingStrategy<T> {
    private final DateTimePath<LocalDateTime> createAtpath;

    @Override
    public void applySorting(T entityPath, JPAQuery<?> query) {
        query.orderBy(createAtpath.desc());
    }
}
