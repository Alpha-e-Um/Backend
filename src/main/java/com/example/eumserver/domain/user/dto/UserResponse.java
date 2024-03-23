package com.example.eumserver.domain.user.dto;

import com.example.eumserver.domain.model.Name;

public record UserResponse(
        String email,
        Name name,
        String avatar
) {
}
