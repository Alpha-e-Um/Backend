package com.example.eumserver.domain.oauth2.attributes;

import com.example.eumserver.domain.user.domain.Name;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2Attributes {

    protected Map<String, Object> attributes;
    protected String provider;
    protected String userNameAttributeName;

    public abstract Name getName();
    public abstract String getEmail();
    public abstract String getAvatar();
    public abstract String getProviderId();
}