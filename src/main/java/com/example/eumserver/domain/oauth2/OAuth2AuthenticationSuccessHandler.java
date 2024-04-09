package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.example.eumserver.global.utils.CookieUtils.addCookie;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${JWT_REFRESH_EXPIRATION_TIME}")
    private long RF_EXPIRATION_IN_MS;

    @Value("${oauth2.client.google.default.redirect-uri}")
    private String DEFAULT_REDIRECT_URI;

    private static final String PARAM_ACCESS_TOKEN = "accesstoken";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(principalDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principalDetails);

        String redirectUrlWithToken = UriComponentsBuilder.fromUriString(DEFAULT_REDIRECT_URI)
                .queryParam(PARAM_ACCESS_TOKEN, accessToken)
                .build().toUriString();

        log.debug("accessToken: {}", accessToken);
        log.debug("refreshToken: {}", refreshToken);
        log.debug("redirect_uri: {}", redirectUrlWithToken);

        addCookie(response, "refreshToken", refreshToken, (int) RF_EXPIRATION_IN_MS);
        getRedirectStrategy().sendRedirect(request, response, redirectUrlWithToken);
    }

}
