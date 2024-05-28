package com.example.eumserver.domain.announcement.team.repository;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamAnnouncementRepository extends JpaRepository<TeamAnnouncement, Long>, TeamAnnouncementCustomRepository {

}
