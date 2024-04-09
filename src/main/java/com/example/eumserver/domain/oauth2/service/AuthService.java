package com.example.eumserver.domain.oauth2.service;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.oauth2.dto.TokenResponse;
import com.example.eumserver.global.error.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.eumserver.global.utils.CookieUtils.addCookie;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${JWT_REFRESH_EXPIRATION_TIME}")
    private long RF_EXPIRATION_IN_MS;

    public TokenResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = "";
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        PrincipalDetails principalDetails;

        if (authentication.getPrincipal() instanceof PrincipalDetails) {
            principalDetails = (PrincipalDetails) authentication.getPrincipal();
        } else throw new CustomException(401, "No refresh token");

        String redisRefreshToken = redisTemplate.opsForValue().get(String.valueOf(principalDetails.getUserId()));
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(403, "Refresh Token is invalid");
        }


        refreshToken = jwtTokenProvider.generateRefreshToken(principalDetails);
        addCookie(response, "refreshToken", refreshToken, (int) RF_EXPIRATION_IN_MS);
        TokenResponse tokenResponse = new TokenResponse(
                jwtTokenProvider.generateAccessToken(principalDetails)
        );

        return tokenResponse;
    }
}
