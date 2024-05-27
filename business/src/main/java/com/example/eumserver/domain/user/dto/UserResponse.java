package com.example.eumserver.domain.user.dto;

import com.example.eumserver.domain.user.Name;

import java.time.LocalDate;

public record UserResponse(
        long userId,
        String email,
        Name name,
        String avatar,
        LocalDate birthday,
        String mbti,
        String phoneNumber
) {
}
