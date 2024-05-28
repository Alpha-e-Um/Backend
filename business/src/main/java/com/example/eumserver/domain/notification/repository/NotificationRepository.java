package com.example.eumserver.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eumserver.domain.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  
}
