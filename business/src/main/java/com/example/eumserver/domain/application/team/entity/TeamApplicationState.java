package com.example.eumserver.domain.application.team.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 지원 상태를 나타내는 Enum 클래스입니다.
 */
public enum TeamApplicationState {
    ALL("all"),
    PENDING("pending"),
    UNDER_REVIEW("under_review"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    WITHDRAWN("withdrawn");

    private final String value;

    TeamApplicationState(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TeamApplicationState from(String value) {
        for (TeamApplicationState state : TeamApplicationState.values()) {
            if (state.getValue().equals(value)) {
                return state;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
