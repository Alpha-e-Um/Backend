package com.example.eumserver.domain.oauth2;

import com.example.eumserver.domain.user.Account;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        registerUser(attributes, registrationId);
        return oAuth2User;
    }

    private void registerUser(Map<String, Object> attributes, String registrationId) {
        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();
        String picture = attributes.get("picture").toString();
        String provider = registrationId.toUpperCase();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User _user = optionalUser.get();
            _user.updateDefaultInfo(email, name, picture);
            userRepository.save(_user);
        }

        Account account = Account.builder()
                .provider(provider)
                .build();
        User user = User.builder()
                .email(email)
                .name(name)
                .avatar(picture)
                .account(account)
                .build();
        userRepository.save(user);
    }
}
