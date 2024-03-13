package com.example.eumserver.domain.user;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;


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

  @Column(nullable = false)
  private String email;

  private String name;
  private String avatar;

  @Builder.Default
  private String role = "ROLE_USER";

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id")
  private Account account;

  public void updateDefaultInfo(String email, String name, String avatar) {
    this.email = email;
    this.name = name;
    this.avatar = avatar;
  }
}

