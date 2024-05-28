package com.example.eumserver.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common error
    INVALID_INPUT_VALUE(400, "COMMON-001", "입력 값이 올바르지 않음"),
    ACCESS_DENIED(403, "COMMON-002", "인가 실패"),
    PAGE_NOT_FOUND(404, "COMMON-003", "요청이 API가 요구하는 URL과 다름"),
    METHOD_NOT_ALLOWED(405, "COMMON-004", "요청이 API가 요구하는 HTTP 메서드와 다름"),
    INTERNAL_SERVER_ERROR(500, "COMMON-005", "정의되지 않은 서버 오류 발생"),

    // Auth error
    INVALID_JWT_TOKEN(401, "AUTH-001", "제공된 토큰이 유효하지 않음"),

    // User error
    USER_NOT_FOUND(400, "USER-001", "유저를 찾을 수 없음"),

    // Resume error
    RESUME_NOT_FOUND(400, "RESUME-001", "이력서를 찾을 수 없음"),

    // Team error
    TEAM_NOT_FOUND(400, "TEAM-001", "팀을 찾을 수 없음"),

    // Invite error
    INVITE_NOT_FOUND(400, "INVITE-001", "초대를 찾을 수 없음"),
    EXPIRED_INVITE(400, "INVITE-002", "해당 초대가 만료됨"),

    TEAM_ANNOUNCEMENT_NOT_FOUND(400, "TEAM-ANNOUNCEMENT-001", "팀 공고를 찾을 수 없음"),

    RESUME_ANNOUNCEMENT_NOT_FOUND(400, "RESUME-ANNOUNCEMENT-001", "이력서 공고를 찾을 수 없음."),

    ALREADY_APPLIED_ANNOUNCEMENT(400, "TEAM-ANNOUNCEMENT-002", "이미 팀에 지원했음");

    private final int status;
    private final String code;
    private final String message;


}
