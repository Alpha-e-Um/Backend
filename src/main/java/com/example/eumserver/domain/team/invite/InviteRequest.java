package com.example.eumserver.domain.team.invite;

import java.util.List;

public record InviteRequest(
        Long teamId,
        List<String> emails
) {
}
