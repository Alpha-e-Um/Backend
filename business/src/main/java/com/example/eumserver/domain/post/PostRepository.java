package com.example.eumserver.domain.post;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface PostRepository<T extends ScoredPost, R extends Post> {
    void updateViews(Long id, Long views);

    default double calculateHackerNewsScore(long views, long hoursSincePosted, double gravity) {
        return (views - 1) / Math.pow((hoursSincePosted + 2), gravity);
    }

    default long getHoursSincePosted(LocalDateTime publishedDate, long currentTimeEpochSeconds) {
        ZoneOffset offset = ZoneOffset.UTC;
        long publishedDateEpochSeconds = publishedDate.toEpochSecond(offset);
        return (currentTimeEpochSeconds - publishedDateEpochSeconds) / 3600;
    }

    default List<T> getPopularPostList(List<R> posts){
        double gravity = 1.8;
        long currentTime = System.currentTimeMillis() / 1000;

        List<T> popularPost = posts.stream()
                .map(announcement -> {
                    long hoursSincePosted = getHoursSincePosted(
                            announcement.getTimeStamp().getCreateDate(), currentTime);
                    double score = calculateHackerNewsScore(
                            announcement.getViews(), hoursSincePosted, gravity);
                    return createScoredPost(announcement, score);
                })
                .sorted(Comparator.comparingDouble(T::getScore).reversed())
                .collect(Collectors.toList());

        return popularPost;
    }

    T createScoredPost(R post, double score);
}
