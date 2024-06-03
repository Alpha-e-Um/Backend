package com.example.eumserver.domain.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 글 목록을 어떤 순으로 받아올지 나타내는 Enum 클래스
 */
@Getter
@RequiredArgsConstructor
public enum PostSortingOption {
    LATEST("latest"), //최신순
    VIEWS("views"), //조회순
    POPULAR("popular"), //인기순
    FAVORITES("favorites"); //찜순

    private final String value;

    @JsonCreator
    public static PostSortingOption from(String value) {
        for (PostSortingOption option : PostSortingOption.values()) {
            if (option.getValue().equals(value)) {
                return option;
            }
        }
        return null;
    }
}
