package com.example.eumserver.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
public class Post {
    @Setter
    @Column
    Long views = 0L;
}
