package com.example.eumserver.domain.user;

import com.example.eumserver.domain.application.entity.Application;
import com.example.eumserver.domain.oauth2.attributes.OAuth2Attributes;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


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

    @Column
    private String avatar;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Builder.Default
    private String role = "ROLE_USER";

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column(length = 4)
    private String mbti;

    @Column
    private LocalDate birthday;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Participant> participants = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @Embedded
    private TimeStamp timeStamp;

    public void updateDefaultInfo(OAuth2Attributes OAuth2Attributes) {
        this.email = OAuth2Attributes.getEmail();
        this.name = new Name(OAuth2Attributes.getName(), "");
        this.avatar = OAuth2Attributes.getAvatar();
    }

    public void updateProfile(Name name, String avatar, String phoneNumber, String mbti, LocalDate birthday) {
        this.name = name;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
        this.mbti = mbti;
        this.birthday = birthday;
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

    public void addTeam(Participant participant) {
        this.participants.add(participant);
    }

    public void addResume(Resume resume) {
        this.resumes.add(resume);
    }
}

