package com.example.eumserver.domain.view;

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

@Slf4j
@RequiredArgsConstructor
public abstract class ViewService<T extends PostRepository, R extends Post> {
    protected final RedisUtil redisUtil;
    protected final T postRepository;

    public void increaseViewCount(String prefix, Long postId, String userId) {
        String key = prefix + ":view::" + postId;
        String userKey = prefix + ":user::" + postId;

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
                T announcement = this.findAnnouncementById(postId);
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
        Set<String> keys = redisUtil.keys("announcement:view:*");
        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Long announcementId = Long.parseLong(key.split("::")[1]);
            String views = (String) redisUtil.getValues(key);
            if (views != null) {
                announcementRepository.updateViews(announcementId, Long.valueOf(views));
            }
        }

        log.info("Views updated at {}", LocalDateTime.now());
    }

    protected abstract T findAnnouncementById(Long id);
}
