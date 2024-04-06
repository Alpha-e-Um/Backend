package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String DEFAULT_REDIRECT_URI = "http://localhost:3000/login/success";
    private static final String PARAM_ACCESS_TOKEN = "accesstoken";
    private static final String PARAM_REFRESH_TOKEN = "refreshtoken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(principalDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principalDetails);

        String redirectUrlWithToken = UriComponentsBuilder.fromUriString(DEFAULT_REDIRECT_URI)
                .queryParam(PARAM_ACCESS_TOKEN, accessToken)
                .queryParam(PARAM_REFRESH_TOKEN, refreshToken)
                .build().toUriString();

        log.debug("accessToken: {}", accessToken);
        log.debug("refreshToken: {}", refreshToken);
        log.debug("redirect_uri: {}", redirectUrlWithToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUrlWithToken);
    }

}
