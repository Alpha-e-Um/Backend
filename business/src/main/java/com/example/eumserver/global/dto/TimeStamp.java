package com.example.eumserver.global.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Embeddable
public class TimeStamp {
    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public void setDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }
}
