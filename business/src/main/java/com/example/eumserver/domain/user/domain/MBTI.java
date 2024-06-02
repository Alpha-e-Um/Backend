package com.example.eumserver.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MBTI {
    INTJ("INTJ"),
    INTP("INTP"),
    ENTJ("ENTJ"),
    ENTP("ENTP"),
    INFJ("INFJ"),
    INFP("INFP"),
    ENFJ("ENFJ"),
    ENFP("ENFP"),
    ISTJ("ISTJ"),
    ISFJ("ISFJ"),
    ESTJ("ESTJ"),
    ESFJ("ESFJ"),
    ISTP("ISTP"),
    ISFP("ISFP"),
    ESTP("ESTP"),
    ESFP("ESFP");

    private final String value;

    MBTI(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MBTI from(String value) {
        for (MBTI mbti : MBTI.values()) {
            if (mbti.value.equals(value)) {
                return mbti;
            }
        }
        return null;
    }
}
