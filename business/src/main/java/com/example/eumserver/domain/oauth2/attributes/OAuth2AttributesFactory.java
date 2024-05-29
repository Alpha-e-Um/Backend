package com.example.eumserver.domain.oauth2.attributes;

import java.util.Map;

public class OAuth2AttributesFactory {

    public static OAuth2Attributes getOauth2Attributes(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return GoogleOAuth2Attributes.builder()
                        .provider(registrationId)
                        .userNameAttributeName(userNameAttributeName)
                        .attributes(attributes)
                        .build();
            default:
                throw new IllegalArgumentException();
        }
    }

}