package com.example.eumserver.domain.notification.domain;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEvent;

@AllArgsConstructor
public class NotificationEvent {
    private String title;
    private String content;
    private NotificationType type;
    private Long targetId;
    private String uri;

    public Notification toEntity() {
        return Notification.builder()
                .title(this.title)
                .content(this.content)
                .type(this.type)
                .status(NotificationStatus.UNREAD)
                .targetId(this.targetId)
                .uri(this.uri)
                .build();
    }
}
