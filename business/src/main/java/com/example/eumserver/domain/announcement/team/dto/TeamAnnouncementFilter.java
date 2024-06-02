package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.domain.post.PostSortingOption;
import lombok.Data;

import java.util.List;

@Data
public class TeamAnnouncementFilter{
    private final int page;
    private final int size;
    private final boolean expired;
    private final PostSortingOption option;
    private final List<OccupationClassification> occupationClassifications;

    public TeamAnnouncementFilter(int page, int size, List<String> occupationClassifications,
                                  String option, boolean expired) {
        this.page = page;
        this.size = size;
        this.expired = expired;
        this.occupationClassifications = occupationClassifications.stream().map(
                OccupationClassification::from
        ).toList();
        this.option = PostSortingOption.from(option);
    }
}
