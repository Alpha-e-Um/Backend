package com.example.eumserver.domain.team.dto;

/**
 * Team 생성 Dto
 */
public record TeamRequest(
    String name,
    String email,
    String phoneNumber,
    String domain,
    String introduction
) { }
