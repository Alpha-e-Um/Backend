package com.example.eumserver.domain.model;

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

    @Column(name = "first_name")
    private String first;

    @Column(name = "last_name")
    private String last;

    public String getFullName() {
        return String.format("%s %s", this.first, this.last);
    }
}