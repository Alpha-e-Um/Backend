package com.example.eumserver.domain.user.dto;

import com.example.eumserver.domain.model.Name;

import java.time.LocalDate;

public record UserResponse(
        String email,
        Name name,
        String avatar,
        LocalDate birthday,
        String mbti,
        String phoneNumber
) {
}
