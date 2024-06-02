package com.example.eumserver.domain.post;

import com.example.eumserver.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 글에 관련된 공통된 기능들을 추상화한 클래스입니다.
 * 조회수 기능, 즐겨찾기 기능을 제공합니다.
 * @param <T> 해당 글의 Repository
 * @param <R> 해당 글의 Entity
 */
@Slf4j
@RequiredArgsConstructor
public abstract class PostService<T extends PostRepository, R extends Post> {
    protected final RedisUtil redisUtil;
    protected final T postRepository;

    protected abstract R findPostById(Long id);

    /**
     * Redis 키에 들어갈 Prefix를 구현합니다.
     * @return Redis Key를 구분지을 수 있는 Prefix
     * ex) teamannouncement
     */
    protected abstract String getPrefix();

    public void increaseViewCount(Long postId, String userId) {
        String prefix = getPrefix();
        String key = prefix + ":view::" + postId;
        String userKey = prefix + ":user::" + postId;

        /**
         * 조회수는 Race Condition이 발생하기 쉬운 환경이라 lock을 걸었습니다.
         * Redis를 이용한 분산락이며, Spin Lock의 형태입니다.
         * Lettuce Redis Client에서는 Spin Lock으로만 가능하여 그렇게 구현하였습니다.
         * 추후, Redission Client로 변경시, 더 효율적인 Pub/Sub 구조의 락으로 변경이 가능합니다.
         * @link https://dkswnkk.tistory.com/681
         * 또한, Redis의 Set 자료구조를 이용하여, 몇분동안 같은 사람이 글을 보더라도
         * 조회수가 오르지 않도록 구성했습니다.
         */
        while (!redisUtil.lock("lock::" + key)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        try {
            if (redisUtil.hasKey(key) && redisUtil.hasKey(userKey)) {
                if (redisUtil.checkValuesSet(userKey, userId)) {
                    redisUtil.increaseValue(key);
                    return;
                } else return;
            } else {
                R announcement = this.findPostById(postId);
                redisUtil.setValue(key, announcement.getViews(),
                        Duration.ofSeconds(480).toMillis(), TimeUnit.SECONDS);
                redisUtil.createValueSet(userKey, Duration.ofSeconds(480).toMillis(), TimeUnit.SECONDS);
            }
        } finally {
            redisUtil.unlock("lock::" + key);
        }
    }

    /**
     * 3분에 한번씩 실제 DB에 조회수를 반영합니다.
     * 글의 수가 많이질 수록 오래걸리는 작업이라 비동기처리 했습니다.
     * 이 부분은 추후 Batch로 빼는 것이 적합합니다.
     */
    @Async
    @Transactional
    @Scheduled(cron = "0 0/3 * * * *")
    protected void applyViewToDB() {
        String prefix = getPrefix();
        Set<String> keys = redisUtil.keys( prefix + ":view:*");
        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Long announcementId = Long.parseLong(key.split("::")[1]);
            String views = (String) redisUtil.getValues(key);
            if (views != null) {
                postRepository.updateViews(announcementId, Long.valueOf(views));
            }
        }

        log.info("{} views updated at {}", prefix, LocalDateTime.now());
    }
}
