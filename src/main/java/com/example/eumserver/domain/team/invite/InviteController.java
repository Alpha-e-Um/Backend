package com.example.eumserver.domain.team.invite;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.team.participant.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping("")
    public ResponseEntity<Invite> inviteUsers(
            @RequestBody InviteRequest inviteRequest
    ) {
        Invite invite = inviteService.inviteUsers(inviteRequest.teamId(), inviteRequest.emails());
        return ResponseEntity.ok(invite);
    }

    @GetMapping("/{invite_id}")
    public ResponseEntity<Participant> acceptInvite(
            @PathVariable("invite_id") Long inviteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Participant participant = inviteService.acceptInvite(inviteId, principalDetails.getUserId());
        return ResponseEntity.ok(participant);
    }
}
