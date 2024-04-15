package com.example.eumserver.domain.team.announcement.repository;

import com.example.eumserver.domain.team.announcement.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, AnnouncementCustomRepository {

}
