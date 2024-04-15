package com.example.eumserver.domain.team.announcement.controller;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementUpdateRequest;
import com.example.eumserver.domain.team.announcement.mapper.AnnouncementMapper;
import com.example.eumserver.domain.team.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AnnouncementResponse> createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcementResponse = announcementService.createAnnouncement(announcementRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(announcementResponse);
    }

    @GetMapping("")
    public ResponseEntity<Page<AnnouncementResponse>> getAnnouncements(
            @RequestParam(name = "page") Integer page,
            @RequestBody AnnouncementFilter announcementFilter
    ) {
        Page<AnnouncementResponse> filteredAnnouncementsWithPaging = announcementService.getFilteredAnnouncementsWithPaging(announcementFilter, page);
        return ResponseEntity.ok(filteredAnnouncementsWithPaging);
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<AnnouncementResponse> getAnnouncement(@PathVariable(name = "announcementId") Long announcementId) {
        Announcement announcement = announcementService.findAnnouncementById(announcementId);
        AnnouncementResponse announcementResponse = AnnouncementMapper.INSTANCE.entityToResponse(announcement);
        return ResponseEntity.ok(announcementResponse);
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<String> updateAnnouncement(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "announcementId") Long announcementId,
            @RequestBody AnnouncementUpdateRequest announcementUpdateRequest
    ) {
        announcementService.updateAnnouncement(announcementUpdateRequest);
        return ResponseEntity.ok("Successfully updated announcement");
    }

    @DeleteMapping("/{announcementId}")
    public ResponseEntity<String> deleteAnnouncement(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "announcementId") Long announcementId
    ) {
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok("Successfully deleted announcement");
    }
}
