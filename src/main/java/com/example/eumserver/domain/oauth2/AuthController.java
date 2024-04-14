package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.oauth2.dto.TokenResponse;
import com.example.eumserver.domain.oauth2.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.reissueToken(request, response);
        return ResponseEntity
                .ok()
                .body(tokenResponse);
    }
}

