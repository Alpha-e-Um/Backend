package com.example.eumserver.domain.jwt;

import com.example.eumserver.domain.user.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PrincipalDetails implements OAuth2User {

    private long userId;
    private String email;
    private Name name;
    private String avatar;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.name.getFullName();
    }

    public Name getNameObject() {
        return this.name;
    }

}
