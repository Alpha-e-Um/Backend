package com.example.eumserver.domain.user;

public record UserResponse(
        String email,
        String name,
        String avatar
) {
}
