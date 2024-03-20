package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.jwt.PrincipleDetails;
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

    private static final String DEFAULT_REDIRECT_URI = "http://localhost:3000/social-login/success";
    private static final String PARAM_TOKEN = "token";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        PrincipleDetails principleDetails = (PrincipleDetails) authentication.getPrincipal();

        // TODO: save refresh token
        String token = jwtTokenProvider.generateAccessToken(principleDetails);

        String redirectUrlWithToken = UriComponentsBuilder.fromUriString(DEFAULT_REDIRECT_URI)
                .queryParam(PARAM_TOKEN, token)
                .build().toUriString();

        log.debug("token: {}", token);
        log.debug("redirect_uri: {}", redirectUrlWithToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUrlWithToken);
    }

}
