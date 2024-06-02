package com.example.eumserver.domain.user.dto;

import com.example.eumserver.domain.user.domain.Name;
import com.example.eumserver.global.domain.Region;

import java.time.LocalDate;

public record UserResponse(
        long userId,
        String email,
        Name name,
        String avatar,
        Region region,
        String school,
        String nickname,
        LocalDate birthday,
        String mbti,
        String phoneNumber
) {
}
