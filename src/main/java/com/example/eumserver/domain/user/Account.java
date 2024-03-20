package com.example.eumserver.domain.user;

import jakarta.persistence.*;
import lombok.*;

/**
 * OAuth2 정보를 담는 Entity
 * @deprecated OAuth2 정보를 담을게 별로 없어서 deprecated 됬습니다.
 */
@Entity
@Table(name = "accounts")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String provider;

}
