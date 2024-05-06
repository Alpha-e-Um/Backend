package com.example.eumserver.domain.team.announcement.repository;

import com.example.eumserver.domain.team.announcement.domain.TeamAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamAnnouncementRepository extends JpaRepository<TeamAnnouncement, Long>, TeamAnnouncementCustomRepository {

}
