package com.example.eumserver.domain.application.dto;

import com.example.eumserver.domain.application.entity.ApplicationState;
import lombok.Data;

@Data
public class MyApplicationFilterRequest {
    private final int page;
    private final ApplicationState state;

    public MyApplicationFilterRequest(int page, String state){
        this.page = page;
        this.state = ApplicationState.from(state);
    }
}
