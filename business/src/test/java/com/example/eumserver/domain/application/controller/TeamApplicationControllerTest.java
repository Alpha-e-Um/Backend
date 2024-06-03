package com.example.eumserver.domain.application.controller;

import com.example.eumserver.BaseIntegrationTest;
import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.repository.TeamAnnouncementRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeamApplicationControllerTest extends BaseIntegrationTest {

    final String BASE_URI = "/api/application";
    @Autowired
    private TeamAnnouncementRepository announcementRepository;

    @BeforeAll
    void setUp() {
        TeamAnnouncement successAnnouncement = TeamAnnouncement.builder()
                .title("스고이데스네")
                .region("충청도")
                .description("마 닥취라 개쉐이야")
                .vacancies(3)
                .occupationClassifications(List.of(
                        OccupationClassification.DEVELOPMENT_GAME,
                        OccupationClassification.DEVELOPMENT_DEVOPS
                ))
                .expiredDate(LocalDateTime.now().plusHours(1))
                .publishedDate(LocalDateTime.parse("2024-10-10T10:00:00"))
                .build();

        TeamAnnouncement test = sut.giveMeOne(TeamAnnouncement.class);

        System.out.println(test.getDescription());

        announcementRepository.save(test);


        TeamAnnouncement failAnnouncement = sut.giveMeBuilder(TeamAnnouncement.class)
                .set("expiredDate", LocalDateTime.now().minusHours(1))
                .set("description", "dd")
                .sample();

//        System.out.println(failAnnouncement.getTitle());

        announcementRepository.save(successAnnouncement);
//        announcementRepository.save(failAnnouncement);
    }

    @Test
    @DisplayName("공고에 지원하기 성공")
    @WithUserDetails("hong@kookmin.ac.kr")
    void applyTeamAnnouncement() throws Exception {
        //given

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(BASE_URI + "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    @DisplayName("내 지원현황 불러오기 성공")
    @WithUserDetails("hong@kookmin.ac.kr")
    void getMyApplication() throws Exception {
        //given


        //when
        final ResultActions resultActions = mockMvc.perform(
                post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

    }


//    @Test
//    @DisplayName("공고 지원 취소하기 성공")
//    void cancelApplication() {
//    }
//
//    @Test
//    @DisplayName("취소할 수 없는 공고에 취소하기")
//    void cancelApplication() {
//    }
}