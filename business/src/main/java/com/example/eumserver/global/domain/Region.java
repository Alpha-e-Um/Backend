package com.example.eumserver.global.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Region {
    SEOUL("서울"),
    BUSAN("부산"),
    INCHEON("인천"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    GYEONGGI("경기도"),
    GANGWON("강원도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    JEONBUK("전라북도"),
    JEONNAM("전라남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEJU("제주도");

    private final String value;

    Region(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Region fromText(String value) {
        for (Region region : Region.values()) {
            if (region.value.equals(value)) {
                return region;
            }
        }
        return null;
    }
}
