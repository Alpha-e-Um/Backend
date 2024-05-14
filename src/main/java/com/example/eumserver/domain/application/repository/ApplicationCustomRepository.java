package com.example.eumserver.domain.application.repository;

import com.example.eumserver.domain.application.dto.MyApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationCustomRepository {
    Page<MyApplicationResponse> getMyApplicationsWithPaging(Long user_id, String state,
                                                            Pageable pageable);
}
