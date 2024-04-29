package com.example.eumserver.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common error
    INVALID_INPUT_VALUE(400, "COMMON-001", "Invalid input value"),
    ACCESS_DENIED(403, "COMMON-002", "Access is denied"),
    ENDPOINT_NOT_FOUND(404, "COMMON-003", "Not found"),
    METHOD_NOT_ALLOWED(405, "COMMON-004", "Method not allowed"),
    INTERNAL_SERVER_ERROR(500, "COMMON-005", "Internal server error"),

    // Auth error
    INVALID_JWT_TOKEN(401, "AUTH-001", "Invalid JWT token"),
    REFRESH_TOKEN_NOT_EXIST(401, "AUTH-002", "There is no refresh token"),
    INVALID_REFRESH_TOKEN(403, "AUTH-003", "Invalid refresh token"),

    // User error
    USER_NOT_FOUND(400, "USER-001", "User not found."),

    // Resume error
    RESUME_NOT_FOUND(400, "RESUME-001", "Resume not found."),

    // Team error
    TEAM_NOT_FOUND(400, "TEAM-001", "Team not found."),

    // Invite error
    INVITE_NOT_FOUND(400, "INVITE-001", "Invite not found."),
    EXPIRED_INVITE(400, "INVITE-002", "Invite is expired");

    private final int status;
    private final String code;
    private final String message;


}
