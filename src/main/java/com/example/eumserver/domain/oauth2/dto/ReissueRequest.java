package com.example.eumserver.domain.oauth2.dto;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(
        @NotBlank String refreshToekn
) {
}
