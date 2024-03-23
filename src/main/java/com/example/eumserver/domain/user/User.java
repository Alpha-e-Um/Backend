package com.example.eumserver.domain.user;

import com.example.eumserver.domain.model.Name;
import com.example.eumserver.domain.oauth2.attributes.OAuth2Attributes;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = "first", column = @Column(name = "first_name")),
          @AttributeOverride(name = "last", column = @Column(name = "last_name"))
  })
  private Name name;
  private String avatar;

  @Builder.Default
  private String role = "ROLE_USER";

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false)
  private String providerId;

  public void updateDefaultInfo(OAuth2Attributes OAuth2Attributes) {
    this.email = OAuth2Attributes.getEmail();
    this.name = new Name(OAuth2Attributes.getName(), "");
    this.avatar = OAuth2Attributes.getAvatar();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(this.role));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

