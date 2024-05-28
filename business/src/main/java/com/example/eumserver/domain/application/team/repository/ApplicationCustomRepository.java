package com.example.eumserver.domain.application.team.repository;

import com.example.eumserver.domain.application.team.dto.MyApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationCustomRepository {
    Page<MyApplicationResponse> getMyApplicationsWithPaging(Long user_id, String state,
                                                            Pageable pageable);
}
