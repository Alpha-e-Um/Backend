package com.example.eumserver.domain.post;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 글쓰기에 관련된 Repository 구현체입니다.
 * @param <T> Hacker News Algorithm의 점수를 저장할 객체입니다.
 * @param <R> 글 Entity를 의미합니다.
 */
public interface PostRepository<T extends ScoredPost, R extends Post> {
    void updateViews(Long id, Long views);

    /**
     * 시간에 흐름에 따라, 순위 변동을 고려하여 인기 게시글을 보여줍니다.
     * @link https://dkswnkk.tistory.com/738
     * @param posts 글 목록
     * @return 인기순으로 정렬된 글 목록
     */
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

    /**
     * Hackers News Algorithm 점수 계산
     * @param views 게시물에 대한 조회 수
     * @param hoursSincePosted 게시물이 게시된 시간과 현재 시간 사이의 차이
     * @param gravity 중력 계수
     * @return
     */
    default double calculateHackerNewsScore(long views, long hoursSincePosted, double gravity) {
        return (views - 1) / Math.pow((hoursSincePosted + 2), gravity);
    }

    /**
     *
     * @param publishedDate 해당 글 게시일
     * @param currentTimeEpochSeconds 현재 시간을 초 단위로 나타낸 값
     * @return 해당 글 게시일과 현재 시간의 차이
     */
    default long getHoursSincePosted(LocalDateTime publishedDate, long currentTimeEpochSeconds) {
        ZoneOffset offset = ZoneOffset.UTC;
        long publishedDateEpochSeconds = publishedDate.toEpochSecond(offset);
        return (currentTimeEpochSeconds - publishedDateEpochSeconds) / 3600;
    }

    T createScoredPost(R post, double score);
}
