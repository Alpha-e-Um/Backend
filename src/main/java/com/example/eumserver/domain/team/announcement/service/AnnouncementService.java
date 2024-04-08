package com.example.eumserver.domain.team.announcement.service;

import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import com.example.eumserver.domain.team.announcement.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public List<AnnouncementResponse> getFilteredAnnouncementsWithPaging(
            AnnouncementFilter filter,
            Pageable pageable
    ) {
        Page<Announcement> filteredAnnouncementsWithPaging = announcementRepository.getFilteredAnnouncementsWithPaging(filter, pageable);
        return null;
    }

}
