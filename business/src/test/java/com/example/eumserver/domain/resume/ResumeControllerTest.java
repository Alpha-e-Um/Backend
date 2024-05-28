package com.example.eumserver.domain.resume;

import com.example.eumserver.BaseIntegrationTest;
import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.user.Name;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResumeControllerTest extends BaseIntegrationTest {
    final String BASE_URI = "/api/resume";

    @Autowired
    private UserRepository userRepository;

    @BeforeTransaction
    void setup() {
        User user = User.builder()
                .name(Name.builder()
                        .first("길동")
                        .last("홍")
                        .build())
                .email("hongildong@naver.com")
                .provider("naver")
                .providerId("1234")
                .build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("이력서 생성 성공")
    @WithUserDetails(value = "hongildong@naver.com")
    void create_resume_success() throws Exception {
        //given
        final ResumeRequest request = new ResumeRequest(
                "개쩌는 이력서",
                "진짜 이력서 즥이네",
                "개발자",
                "백엔드",
                4.5,
                4.5,
                true,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(BASE_URI)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(request.title()))
                .andExpect(jsonPath("$.jobCategory").value(request.jobCategory()))
                .andExpect(jsonPath("$.jobSubcategory").value(request.jobSubcategory()))
                .andExpect(jsonPath("$.introduction").value(request.description()));
    }


}