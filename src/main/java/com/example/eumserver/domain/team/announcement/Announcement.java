package com.example.eumserver.domain.team.announcement;

import com.example.eumserver.global.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "team_announcements")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_announcement_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int vacancies;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(name = "date_expired", nullable = false)
    private LocalDate expiredDate;

    @Column(name = "date_published")
    private LocalDate publishedDate;

    @Embedded
    private TimeStamp timeStamp;

    public boolean isExpired() {
        LocalDate now = LocalDate.now();
        return expiredDate.isBefore(now);
    }

    public boolean isPublished() {
        return publishedDate != null;
    }

}
