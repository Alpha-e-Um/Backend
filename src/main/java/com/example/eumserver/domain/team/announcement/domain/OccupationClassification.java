package com.example.eumserver.domain.team.announcement.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 모집 공고의 직종 분류를 모아둔 Enum Class 입니다.
 */
public enum OccupationClassification {
    DESIGN_UI_UX("design_ui_ux"),
    DESIGN_ILLUSTRATION_CHARACTER("design_illustration_character"),
    DESIGN_MOVIE_MOTION_GRAPHIC("design_movie_motion_graphic"),
    DESIGN_PRODUCT_PACKAGE("design_product_package"),
    DESIGN_PASSION("design_passion"),
    DESIGN_CRAFTS("design_crafts"),
    DESIGN_GRAPHIC("design_graphic"),

    DEVELOPMENT_FRONTEND("development_frontend"),
    DEVELOPMENT_BACKEND("development_backend"),
    DEVELOPMENT_AI("development_ai"),
    DEVELOPMENT_DEVOPS("development_devops"),
    DEVELOPMENT_SECURITY("development_security"),
    DEVELOPMENT_GAME("development_game"),
    DEVELOPMENT_EMBEDDED("development_embedded"),
    DEVELOPMENT_DBA("development_dba"),

    PLAN("plan"),

    MEDIA_EDITING_MOVIE("media_editing_movie"),
    MEDIA_FILMING_MOVIE("media_filming_movie"),
    MEDIA_PD("media_pd"),
    MEDIA_PHOTOGRAPHER("media_photographer"),
    MEDIA_SOUND_ENGINEER("media_sound_engineer"),

    MARKETING_MANAGEMENT("marketing_management"),
    MARKETING_PROMOTION("marketing_promotion"),
    MARKETING_CONSULTING("marketing_consulting"),

    TRANSLATION_ENGLISH("translation_english"),
    TRANSLATION_JAPANESE("translation_japanese"),
    TRANSLATION_CHINESE("translation_chinese"),
    TRANSLATION_SPANISH("translation_spanish"),
    TRANSLATION_ALABIC("translation_alabic"),
    TRANSLATION_HINDI("translation_hindi"),
    TRANSLATION_FRANCH("translation_franch"),
    TRANSLATION_ETC("translation_etc"),

    ETC("etc");

    private final String value;

    OccupationClassification(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OccupationClassification from(String value) {
        for (OccupationClassification occupationClassification: OccupationClassification.values()) {
            if (occupationClassification.getValue().equals(value)) {
                return occupationClassification;
            }
        }
        return null;
    }
}
