package com.example.eumserver.domain.team;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.team.dto.TeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Team> createTeam(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody TeamRequest teamRequest) {
        Team team = teamService.createTeam(principalDetails.getUserId(), teamRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(team);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(
            @PathVariable long teamId
    ) {
        Team team = teamService.findById(teamId);
        return ResponseEntity.ok(team);
    }

    @DeleteMapping("/{teamId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> deleteTeamById(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable long teamId
    ) {
        teamService.deleteTeam(principalDetails.getUserId(), teamId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(null);
    }

}
