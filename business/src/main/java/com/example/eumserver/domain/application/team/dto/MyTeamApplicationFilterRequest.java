package com.example.eumserver.domain.application.team.dto;

import com.example.eumserver.domain.application.team.entity.ApplicationState;
import lombok.Data;

@Data
public class MyTeamApplicationFilterRequest {
    private final int page;
    private final ApplicationState state;

    public MyTeamApplicationFilterRequest(int page, String state){
        this.page = page;
        this.state = ApplicationState.from(state);
    }
}
