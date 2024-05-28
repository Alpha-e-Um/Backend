package com.example.eumserver.domain.notification.service;

import com.example.eumserver.domain.notification.domain.Notification;
import com.example.eumserver.domain.notification.domain.NotificationEvent;
import com.example.eumserver.domain.notification.repository.NotificationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithConversion(NotificationEvent notificationEvent) {
    Notification notification = notificationEvent.toEntity();
    notificationRepository.save(notification);
  }
  
}
