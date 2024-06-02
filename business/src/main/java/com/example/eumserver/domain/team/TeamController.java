package com.example.eumserver.domain.team;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.team.dto.TeamRequest;
import com.example.eumserver.domain.team.participant.ParticipantService;
import com.example.eumserver.domain.user.dto.UserResponse;
import com.example.eumserver.global.annotation.MaxFileSize;
import com.example.eumserver.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final ParticipantService participantService;

    private static final int MAX_TEAM_LOGO_SIZE = 3 * 1024 * 1024; // 3MB

    @MaxFileSize(value = MAX_TEAM_LOGO_SIZE)
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResult<Team>> createTeam(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestPart(name = "file", required = false) MultipartFile logo,
            @RequestPart(name = "json") TeamRequest teamRequest) {
        Team team = teamService.createTeam(principalDetails.getUserId(), teamRequest, logo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("팀 생성 성공", team));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResult<Team>> getTeamById(
            @PathVariable long teamId
    ) {
        Team team = teamService.findById(teamId);
        return ResponseEntity
                .ok(new ApiResult<>("팀 조회 성공", team));
    }

    @DeleteMapping("/{teamId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResult<?>> deleteTeamById(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable long teamId
    ) {
        teamService.deleteTeam(principalDetails.getUserId(), teamId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResult<>("팀 삭제 성공"));
    }

    @GetMapping("/{teamId}/participant")
    public ResponseEntity<ApiResult<List<UserResponse>>> getAllParticipant(
            @PathVariable long teamId
    ) {
        List<UserResponse> userResponses = participantService.getAllParticipantByTeamId(teamId);
        return ResponseEntity.ok(new ApiResult<>("팀 참가자 조회 성공", userResponses));
    }

}
