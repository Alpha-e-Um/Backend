package com.example.eumserver.domain.oauth2.service;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.oauth2.dto.TokenResponse;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.example.eumserver.domain.jwt.JwtTokenProvider.RF_EXPIRATION_IN_MS;
import static com.example.eumserver.global.utils.CookieUtils.COOKIE_REFRESH_TOKEN;
import static com.example.eumserver.global.utils.CookieUtils.addCookie;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        PrincipalDetails principalDetails;

        if (authentication.getPrincipal() instanceof PrincipalDetails) {
            principalDetails = (PrincipalDetails) authentication.getPrincipal();
        } else throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);

        String redisRefreshToken = redisTemplate.opsForValue().get(String.valueOf(principalDetails.getUserId()));
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(principalDetails);
        addCookie(
                response,
                COOKIE_REFRESH_TOKEN,
                newRefreshToken,
                (int) TimeUnit.MILLISECONDS.toSeconds(RF_EXPIRATION_IN_MS));

        return new TokenResponse(
                jwtTokenProvider.generateAccessToken(principalDetails)
        );
    }

    public void logout(String accessToken, String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String userId = claims.getSubject();
        redisTemplate.opsForValue().getAndDelete(userId);

        redisTemplate.opsForValue().set(accessToken, "logout");
    }

}
