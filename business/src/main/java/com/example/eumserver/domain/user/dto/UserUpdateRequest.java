package com.example.eumserver.domain.user.dto;

import com.example.eumserver.domain.user.domain.MBTI;
import com.example.eumserver.global.domain.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserUpdateRequest (
        @NotBlank String firstName,
        String lastName,
        String avatar,
        Region region,
        MBTI mbti,
        String school,
        String nickname,
        LocalDate birthday,
        @Pattern(regexp = "[0-9]{10,11}") String phoneNumber

) { }