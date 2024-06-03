package com.example.eumserver.domain.user.domain;

import com.example.eumserver.domain.application.team.entity.TeamApplication;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.user.dto.UserUpdateRequest;
import com.example.eumserver.global.domain.Region;
import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String nickname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "first_name")),
            @AttributeOverride(name = "last", column = @Column(name = "last_name"))
    })
    private Name name;

    private String avatar;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Builder.Default
    private String role = "ROLE_USER";

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    private String school;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    private LocalDate birthday;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Participant> participants = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamApplication> applications = new ArrayList<>();

    @Embedded
    private TimeStamp timeStamp;

    public void updateProfile(UserUpdateRequest request) {
        this.name = new Name(request.firstName(), request.lastName());
        this.avatar = request.avatar();
        this.region = request.region();
        this.mbti = request.mbti();
        this.school = request.school();
        this.nickname = request.nickname();
        this.birthday = request.birthday();
        this.phoneNumber = request.phoneNumber();
    }

    public void addTeam(Participant participant) {
        this.participants.add(participant);
    }

    public void addResume(Resume resume) {
        this.resumes.add(resume);
    }
}

