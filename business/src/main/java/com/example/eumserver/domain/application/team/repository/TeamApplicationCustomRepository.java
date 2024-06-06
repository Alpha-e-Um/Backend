package com.example.eumserver.domain.application.team.repository;

import com.example.eumserver.domain.application.team.dto.MyTeamApplicationResponse;
import com.example.eumserver.domain.application.team.entity.ApplicationState;
import com.example.eumserver.domain.application.team.entity.TeamApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamApplicationCustomRepository {

    Page<MyTeamApplicationResponse> getMyApplicationsWithPaging(Long userId, ApplicationState state,
                                                                Pageable pageable);
    boolean checkApplicationExist(Long userId, Long announcementId);
    TeamApplication getApplicationWithState(Long userId, Long announcementId, ApplicationState state);

}
