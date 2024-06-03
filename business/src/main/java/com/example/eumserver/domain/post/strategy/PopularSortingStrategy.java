package com.example.eumserver.domain.post.strategy;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PopularSortingStrategy<T extends EntityPathBase<?>> implements SortingStrategy<T> {
    private final NumberPath<Long> viewsPath;
    private final DateTimePath<LocalDateTime> createdAtPath;

    @Override
    public void applySorting(T entityPath, JPAQuery<?> query) {
        query.orderBy(viewsPath.desc(), createdAtPath.desc());
    }
}
