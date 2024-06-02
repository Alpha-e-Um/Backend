package com.example.eumserver.domain.oauth2.service;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.oauth2.attributes.OAuth2Attributes;
import com.example.eumserver.domain.oauth2.attributes.OAuth2AttributesFactory;
import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.debug("[loadUser] attributes = {}", attributes);
        OAuth2Attributes oAuth2Attributes = OAuth2AttributesFactory.getOauth2Attributes(registrationId, userNameAttributeName, attributes);

        Optional<User> _user = userRepository.findByEmail(oAuth2Attributes.getEmail());
        User user = _user.map(value -> updateUser(value, oAuth2Attributes)).orElseGet(() -> registerUser(oAuth2Attributes));

        return new PrincipalDetails(
                user.getId(),
                user.getEmail(),
                user.getName(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private User registerUser(OAuth2Attributes oAuth2Attributes) {
        User user = User.builder()
                .email(oAuth2Attributes.getEmail())
                .name(oAuth2Attributes.getName())
                .avatar(oAuth2Attributes.getAvatar())
                .provider(oAuth2Attributes.getProvider())
                .providerId(oAuth2Attributes.getProviderId())
                .build();
        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2Attributes OAuth2Attributes) {
        user.updateDefaultInfo(OAuth2Attributes);
        return userRepository.save(user);
    }
}
