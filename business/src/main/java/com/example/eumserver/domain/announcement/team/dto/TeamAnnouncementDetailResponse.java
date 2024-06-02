package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TeamAnnouncementDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String region;
    private LocalDateTime createDate;
    private List<OccupationClassification> occupationClassifications;
    private Long views;
    private Long teamId;
    private String teamLogo;

    public void setViews(Long views) {
        this.views = views;
    }
}
