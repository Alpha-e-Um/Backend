package com.example.eumserver.domain.team.announcement.controller;

import com.example.eumserver.BaseIntegrationTest;
import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.announcement.domain.TeamAnnouncement;
import com.example.eumserver.domain.team.announcement.domain.OccupationClassification;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementUpdateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AnnouncementControllerTest extends BaseIntegrationTest {

    final String BASE_URI = "/api/team/{teamId}/announcement";

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("공고_생성_성공")
    @WithUserDetails()
    void create_announcement_success() throws Exception {
        Team team = createTeam();

        TeamAnnouncementRequest announcementRequest =
                new TeamAnnouncementRequest(
                        "title",
                        "description",
                        5,
                        List.of(
                                OccupationClassification.DEVELOPMENT_GAME,
                                OccupationClassification.DEVELOPMENT_DEVOPS
                        ),
                        false,
                        LocalDateTime.parse("2024-10-10T10:00:00")
                );

        ResultActions resultActions = mockMvc.perform(
                post(BASE_URI, team.getId())
                        .content(objectMapper.writeValueAsString(announcementRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(announcementRequest.title()))
                .andExpect(jsonPath("$.data.description").value(announcementRequest.description()));
    }

    @Test
    @DisplayName("공고_단건_조회-성공")
    @WithMockUser
    void get_announcement_success() throws Exception {
        Team team = createTeam();
        TeamAnnouncement announcement = createAnnouncement(team);

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URI + "/{announcementId}", team.getId(), announcement.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(announcement.getTitle()))
                .andExpect(jsonPath("$.data.description").value(announcement.getDescription()));
    }

    @Test
    @DisplayName("공고_필터링+페이징_조회-성공")
    @WithMockUser
    void get_filtered_announcement_with_paging_success() throws Exception {
        Team team = createTeam();
        TeamAnnouncement announcement = createAnnouncement(team);

        TeamAnnouncementFilter filter = new TeamAnnouncementFilter(
                false,
                List.of(OccupationClassification.DEVELOPMENT_BACKEND, OccupationClassification.DEVELOPMENT_DEVOPS)
        );

        ResultActions resultActions = mockMvc.perform(
                get(BASE_URI, team.getId())
                        .param("page", "0")
                        .content(objectMapper.writeValueAsString(filter))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.number").value(0))
                .andExpect(jsonPath("$.data.size").value(12))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.content.[0].title").value(announcement.getTitle()))
                .andExpect(jsonPath("$.data.content.[0].description").value(announcement.getDescription()));
    }

    @Test
    @DisplayName("공고_수정-성공")
    @WithMockUser
    void update_announcement_success() throws Exception {
        Team team = createTeam();
        TeamAnnouncement announcement = createAnnouncement(team);

        TeamAnnouncementUpdateRequest announcementUpdateRequest = new TeamAnnouncementUpdateRequest(
                "new title",
                "new description",
                2,
                List.of(OccupationClassification.DEVELOPMENT_BACKEND,
                        OccupationClassification.DEVELOPMENT_DEVOPS,
                        OccupationClassification.DEVELOPMENT_SECURITY,
                        OccupationClassification.DEVELOPMENT_DBA),
                true,
                LocalDateTime.parse("2024-12-12T10:00:00")
        );

        ResultActions resultActions = mockMvc.perform(
                put(BASE_URI + "/{announcementId}", team.getId(), announcement.getId())
                        .content(objectMapper.writeValueAsString(announcementUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk());

        TeamAnnouncement updatedAnnouncement = em.find(TeamAnnouncement.class, announcement.getId());
        assertEquals(announcementUpdateRequest.title(), updatedAnnouncement.getTitle());
        assertEquals(announcementUpdateRequest.description(), updatedAnnouncement.getDescription());
        assertEquals(announcementUpdateRequest.vacancies(), updatedAnnouncement.getVacancies());
        assertEquals(4, updatedAnnouncement.getOccupationClassifications().size());
        assertEquals(announcementUpdateRequest.publish(), updatedAnnouncement.isPublished());
        assertEquals(announcementUpdateRequest.expiredDate(), updatedAnnouncement.getExpiredDate());
    }

    @Test
    @DisplayName("공고_삭제-성공")
    @WithMockUser
    void delete_announcement_success() throws Exception {
        Team team = createTeam();
        TeamAnnouncement announcement = createAnnouncement(team);

        ResultActions resultActions = mockMvc.perform(
                delete(BASE_URI + "/{announcementId}", team.getId(), announcement.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true));
    }

    private Team createTeam() {
        Team team = Team.builder()
                .name("name")
                .build();
        em.persist(team);
        return team;
    }

    private TeamAnnouncement createAnnouncement(Team team) {
        TeamAnnouncement announcement = TeamAnnouncement.builder()
                .title("test_title")
                .description("test_description")
                .vacancies(1)
                .occupationClassifications(
                        List.of(OccupationClassification.DEVELOPMENT_BACKEND,
                                OccupationClassification.DEVELOPMENT_DEVOPS,
                                OccupationClassification.DEVELOPMENT_SECURITY)
                )
                .expiredDate(LocalDateTime.parse("2024-10-10T10:00:00"))
                .team(team)
                .build();
        em.persist(announcement);
        return announcement;
    }

}