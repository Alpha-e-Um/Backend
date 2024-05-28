package com.example.eumserver.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public record UserUpdateRequest (
        @NotBlank String firstName,
        String lastName,
        String avatar,
        LocalDate birthday,
        @Size(min = 4, max = 4) String mbti,
        @Pattern(regexp = "[0-9]{10,11}") String phoneNumber

) {
    private static final List<String> allowedMBTIs = Arrays.asList(
            "INTJ", "INTP", "ENTJ", "ENTP",
            "INFJ", "INFP", "ENFJ", "ENFP",
            "ISTJ", "ISFJ", "ESTJ", "ESFJ",
            "ISTP", "ISFP", "ESTP", "ESFP"
    );

    public boolean isValid(String mbti) {
        return mbti == null || allowedMBTIs.contains(mbti.toUpperCase());
    }
}