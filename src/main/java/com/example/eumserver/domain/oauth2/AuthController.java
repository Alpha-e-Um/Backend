package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.oauth2.dto.ReissueRequest;
import com.example.eumserver.domain.oauth2.dto.TokenResponse;
import com.example.eumserver.domain.oauth2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody @Valid ReissueRequest reissueRequest) {
        TokenResponse tokenResponse = authService.reissueToken(reissueRequest.refreshToekn());
        return ResponseEntity
                .ok()
                .body(tokenResponse);
    }
}

