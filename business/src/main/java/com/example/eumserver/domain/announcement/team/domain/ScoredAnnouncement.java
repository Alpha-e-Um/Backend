package com.example.eumserver.domain.announcement.team.domain;

import com.example.eumserver.domain.post.ScoredPost;

public class ScoredAnnouncement implements ScoredPost {
    private final TeamAnnouncement announcement;
    private final double score;

    public ScoredAnnouncement(TeamAnnouncement announcement, double score) {
        this.announcement = announcement;
        this.score = score;
    }

    public TeamAnnouncement getAnnouncement() {
        return announcement;
    }

    @Override
    public double getScore() {
        return score;
    }
}
