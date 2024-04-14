package com.example.eumserver.domain.team.announcement.service;

import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.domain.OccupationClassification;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import com.example.eumserver.domain.team.announcement.repository.AnnouncementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AnnouncementServiceTest {

    @Autowired
    AnnouncementService announcementService;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Test
    @DisplayName("공고 조회 / 성공")
    void get_announcement_success() {
        String title = "test announcement";
        String description = "test description";

        Announcement announcement = Announcement.builder()
                .title(title)
                .description(description)
                .expiredDate(LocalDate.now())
                .occupationClassifications(
                        Arrays.asList(
                                OccupationClassification.DEVELOPMENT_DBA,
                                OccupationClassification.DEVELOPMENT_BACKEND,
                                OccupationClassification.DEVELOPMENT_SECURITY
                        )
                )
                .build();

        announcementRepository.save(announcement);
        AnnouncementFilter filter = new AnnouncementFilter(
                Arrays.asList(OccupationClassification.DEVELOPMENT_DBA, OccupationClassification.DEVELOPMENT_BACKEND)
        );
        int pageNum = 0;

        Page<AnnouncementResponse> filteredAnnouncementsWithPaging = announcementService.getFilteredAnnouncementsWithPaging(
                filter,
                pageNum
        );
        assertEquals(1, filteredAnnouncementsWithPaging.getTotalPages());
        assertEquals(1, filteredAnnouncementsWithPaging.getTotalElements());

        List<AnnouncementResponse> content = filteredAnnouncementsWithPaging.getContent();
        assertEquals(1, content.size());
        assertEquals(title, content.get(0).title());
        assertEquals(description, content.get(0).description());
    }

}