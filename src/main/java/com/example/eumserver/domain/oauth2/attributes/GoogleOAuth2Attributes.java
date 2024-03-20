package com.example.eumserver.domain.oauth2.attributes;

import lombok.Builder;

import java.util.Map;

public class GoogleOAuth2Attributes extends AbstractOAuth2Attributes {

    @Builder
    public GoogleOAuth2Attributes(Map<String, Object> attributes, String userNameAttributeName) {
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public String getOAuth2Id() {
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
    public String getName() {
        return attributes.get("name").toString();
    }

}
