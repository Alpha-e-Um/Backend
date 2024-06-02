package com.example.eumserver.domain.post;

import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
public class Post {
    @Setter
    @Column
    Long views = 0L;

    @Embedded
    private TimeStamp timeStamp;

}
