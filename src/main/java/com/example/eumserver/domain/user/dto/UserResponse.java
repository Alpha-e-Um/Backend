package com.example.eumserver.domain.user.dto;

public record UserResponse(
        String email,
        String name,
        String avatar
) {
}
