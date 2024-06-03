package com.example.eumserver.domain.post.strategy;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ViewsSortingStrategy<T extends EntityPathBase<?>> implements SortingStrategy<T> {
    private final NumberPath<Long> viewsPath;

    @Override
    public void applySorting(T entityPath, JPAQuery<?> query) {
        query.orderBy(viewsPath.desc());
    }
}
