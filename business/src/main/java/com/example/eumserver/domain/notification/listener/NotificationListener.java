package com.example.eumserver.domain.notification.listener;

import com.example.eumserver.domain.notification.domain.NotificationEvent;
import com.example.eumserver.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

  private final NotificationService notificationService;

  @TransactionalEventListener
  public void saveNotification(NotificationEvent notificationEvent) {
    notificationService.saveWithConversion(notificationEvent);
  }
 
}
