package com.example.eumserver.domain.application.repository;

import com.example.eumserver.domain.application.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.entity.ApplicationState;
import com.example.eumserver.domain.application.entity.TeamApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationCustomRepository {
    Page<MyApplicationResponse> getMyApplicationsWithPaging(Long userId, ApplicationState state,
                                                            Pageable pageable);

    boolean checkApplicationExist(Long userId, Long announcementId);
    TeamApplication getCancelApplication(Long userId, Long announcementId);

}
