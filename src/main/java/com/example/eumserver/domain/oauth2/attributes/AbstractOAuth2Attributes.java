package com.example.eumserver.domain.oauth2.attributes;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class AbstractOAuth2Attributes {

    protected Map<String, Object> attributes;
    protected String userNameAttributeName;

    public abstract String getName();
    public abstract String getEmail();
    public abstract String getAvatar();
    public abstract String getOAuth2Id();
}