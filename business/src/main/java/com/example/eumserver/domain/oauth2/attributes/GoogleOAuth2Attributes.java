package com.example.eumserver.domain.oauth2.attributes;

import com.example.eumserver.domain.user.domain.Name;
import lombok.Builder;

import java.util.Map;

public class GoogleOAuth2Attributes extends OAuth2Attributes {

    @Builder
    public GoogleOAuth2Attributes(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        this.provider = provider;
        this.userNameAttributeName = userNameAttributeName;
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get(this.userNameAttributeName).toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getAvatar() {
        return attributes.get("picture").toString();
    }

    @Override
    public Name getName() {
        String familyName = attributes.get("family_name").toString();
        String givenName = attributes.get("given_name").toString();
        return new Name(familyName, givenName);
    }

}
