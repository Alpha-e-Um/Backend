package com.example.eumserver.domain.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public static final String ATTRIBUTE_TOKEN_ERROR = "token_error";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = tokenProvider.resolveAccessToken(request);
        log.debug("receive access token: {}", accessToken);

        try {
            if (StringUtils.hasText(accessToken) && !isTokenBlacked(accessToken) && tokenProvider.validateToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtTokenInvalidException jwtTokenInvalidException) {
            request.setAttribute(ATTRIBUTE_TOKEN_ERROR, jwtTokenInvalidException);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenBlacked(String accessToken) {
        String isLogout = redisTemplate.opsForValue().get(accessToken);
        return isLogout != null && isLogout.equals("logout");
    }
}
