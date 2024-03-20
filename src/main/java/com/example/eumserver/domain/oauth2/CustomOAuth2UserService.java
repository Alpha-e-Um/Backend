package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.jwt.PrincipleDetails;
import com.example.eumserver.domain.oauth2.attributes.AbstractOAuth2Attributes;
import com.example.eumserver.domain.oauth2.attributes.OAuth2AttributesFactory;
import com.example.eumserver.domain.user.User;
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
        AbstractOAuth2Attributes abstractOAuth2Attributes = OAuth2AttributesFactory.getOauth2Attributes(registrationId, attributes, userNameAttributeName);

        User user = userRepository.findByEmail(abstractOAuth2Attributes.getEmail()).orElse(null);
        if (user != null) {
            user = updateUser(user, abstractOAuth2Attributes);
        } else {
            user = registerUser(registrationId, abstractOAuth2Attributes);
        }

        return new PrincipleDetails(
                user.getEmail(),
                user.getName(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    private User registerUser(String registrationId, AbstractOAuth2Attributes abstractOAuth2Attributes) {
        User user = User.builder()
                .email(abstractOAuth2Attributes.getEmail())
                .name(abstractOAuth2Attributes.getName())
                .avatar(abstractOAuth2Attributes.getAvatar())
                .provider(registrationId)
                .oAuth2Id(abstractOAuth2Attributes.getOAuth2Id())
                .build();
        return userRepository.save(user);
    }

    private User updateUser(User user, AbstractOAuth2Attributes abstractOAuth2Attributes) {
        user.updateDefaultInfo(abstractOAuth2Attributes);
        return userRepository.save(user);
    }
}
