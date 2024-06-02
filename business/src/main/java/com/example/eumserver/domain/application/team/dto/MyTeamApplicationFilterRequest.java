package com.example.eumserver.domain.application.team.dto;

import com.example.eumserver.domain.application.team.entity.TeamApplicationState;
import lombok.Data;

@Data
public class MyTeamApplicationFilterRequest {
    private final int page;
    private final TeamApplicationState state;

    public MyTeamApplicationFilterRequest(int page, String state){
        this.page = page;
        this.state = TeamApplicationState.from(state);
    }
}
