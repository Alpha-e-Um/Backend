package com.example.eumserver.domain.team.dto;

public record TeamResponse(
        Long id,
        String name,
        String logo,
        String introduction,
        boolean isOwner
) {
}
