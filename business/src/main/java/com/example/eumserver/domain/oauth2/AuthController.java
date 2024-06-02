package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.oauth2.dto.TokenResponse;
import com.example.eumserver.domain.oauth2.service.AuthService;
import com.example.eumserver.global.dto.ApiResult;
import com.example.eumserver.global.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResult<TokenResponse>> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = authService.reissueToken(request, response);
        return ResponseEntity
                .ok()
                .body(new ApiResult<>("토큰 재생성 성공", tokenResponse));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResult<?>> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request.getHeader("Authorization"));
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        authService.logout(accessToken, refreshToken);

        CookieUtils.deleteCookie(request, response, CookieUtils.COOKIE_REFRESH_TOKEN);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResult<>("로그아웃 성공"));
    }
}

