package com.example.eumserver.domain.post.strategy;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;

public interface SortingStrategy<T extends EntityPath<?>> {
    void applySorting(T entityPath, JPAQuery<?> query);
}