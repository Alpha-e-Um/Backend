package com.example.eumserver.domain.announcement.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TeamAnnouncementResponseWrapper {
    private boolean empty;
    private boolean first;
    private boolean last;
    private Integer totalPages;
    private List<TeamAnnouncementResponse> content;
}
