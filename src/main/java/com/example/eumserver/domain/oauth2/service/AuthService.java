package com.example.eumserver.domain.oauth2.service;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.oauth2.dto.TokenResponse;
import com.example.eumserver.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenResponse reissueToken(String refreshToken) {
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

        TokenResponse tokenResponse = new TokenResponse(
                jwtTokenProvider.generateAccessToken(principalDetails),
                jwtTokenProvider.generateRefreshToken(principalDetails)
        );

        return tokenResponse;
    }
}
