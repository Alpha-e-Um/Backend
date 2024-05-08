package com.example.eumserver.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"first", "last"})
public class Name {

    @Column(name = "first_name", nullable = false)
    private String first;

    @Column(name = "last_name")
    private String last;

    public String getFullName() {
        return String.format("%s %s", this.first, this.last);
    }
}