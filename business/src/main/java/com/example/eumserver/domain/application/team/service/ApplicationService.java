package com.example.eumserver.domain.application.team.service;

import com.example.eumserver.domain.application.team.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.team.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private ApplicationRepository applicationRepository;

    public Page<MyApplicationResponse> getMyApplications(Long user_id) {
        Pageable paging = PageRequest.of(0, 10);
        Page<MyApplicationResponse> list = applicationRepository
                .getMyApplicationsWithPaging(user_id, "all", paging);

        return list;
    }

    public void applyTeam() {

    }
}
