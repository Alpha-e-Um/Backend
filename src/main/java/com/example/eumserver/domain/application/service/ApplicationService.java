package com.example.eumserver.domain.application.service;

import com.example.eumserver.domain.application.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private ApplicationRepository applicationRepository;

//    public Page<MyApplicationResponse> getMyApplications(Pageable pageable) {
//
//
//    }

    public void applyTeam() {

    }
}
