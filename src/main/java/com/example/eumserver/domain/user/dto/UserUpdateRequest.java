package com.example.eumserver.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest (
        @Email String email,
        @Pattern(regexp = "[0-9]{10,11}") String phoneNumber
) {

}