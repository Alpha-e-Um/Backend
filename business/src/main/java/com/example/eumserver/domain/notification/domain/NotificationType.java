package com.example.eumserver.domain.notification.domain;

/**
 * 알림의 타입을 나타내는 enum class
 */
public enum NotificationType {
  TEAM_INVITE_REQUEST, // 팀 초대
  TEAM_INVITE_ACCEPT, // 팀 초대 수락
  TEAM_INVITE_REJECT, // 팀 초대 거절

  TEAM_JOIN_REQUEST, // 팀 가입
  TEAM_JOIN_ACCEPT, // 팀 가입 수락
  TEAM_JOIN_REJECT, // 팀 가입 거절

  TEAM_ANNOUNCEMENT_REQUEST, // 팀 공고 지원
  TEAM_ANNOUNCEMENT_ACCEPT, // 팀 공고 지원 합격
  TEAM_ANNOUNCEMENT_REJECT, // 팀 공고 지원 불합격

  RESUME_ANNOUNCEMENT_REQUEST, // 이력서 공고 채용
  RESUME_ANNOUNCEMENT_ACCEPT, // 이력서 공고 채용 수락
  RESUME_ANNOUNCEMENT_REJECT, // 이력서 공고 채용 거절
}
