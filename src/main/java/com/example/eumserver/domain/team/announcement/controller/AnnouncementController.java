package com.example.eumserver.domain.team.announcement.controller;

import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementUpdateRequest;
import com.example.eumserver.domain.team.announcement.mapper.AnnouncementMapper;
import com.example.eumserver.domain.team.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/team/{teamId}/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @PathVariable(name = "teamId") Long teamId,
            @RequestBody AnnouncementRequest announcementRequest
    ) {
        log.debug("announcement request: {}", announcementRequest);
        AnnouncementResponse announcementResponse = announcementService.createAnnouncement(teamId, announcementRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(announcementResponse);
    }

    @GetMapping("")
    public ResponseEntity<Page<AnnouncementResponse>> getAnnouncements(
            @PathVariable(name = "teamId") Long teamId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestBody AnnouncementFilter announcementFilter
    ) {
        Page<AnnouncementResponse> filteredAnnouncementsWithPaging = announcementService.getFilteredAnnouncementsWithPaging(teamId, page, announcementFilter);
        return ResponseEntity.ok(filteredAnnouncementsWithPaging);
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> getAnnouncement(@PathVariable(name = "announcementId") Long announcementId) {
        Announcement announcement = announcementService.findAnnouncementById(announcementId);
        AnnouncementResponse announcementResponse = AnnouncementMapper.INSTANCE.entityToResponse(announcement);
        return ResponseEntity.ok(announcementResponse);
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<Void> updateAnnouncement(
            @PathVariable(name = "announcementId") Long announcementId,
            @RequestBody AnnouncementUpdateRequest announcementUpdateRequest
    ) {
        announcementService.updateAnnouncement(announcementId, announcementUpdateRequest);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{announcementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAnnouncement(
            @PathVariable(name = "announcementId") Long announcementId
    ) {
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
