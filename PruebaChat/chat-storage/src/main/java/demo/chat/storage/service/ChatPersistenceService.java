package demo.chat.storage.service;

import demo.chat.model.ChatMessage;
import demo.chat.storage.entity.ChatMessageEntity;
import demo.chat.storage.repo.ChatMessageRepository;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ChatPersistenceService {

    private final ChatMessageRepository repo;
    private final RedisTemplate<String, Object> redis;

    public ChatPersistenceService(ChatMessageRepository repo,
                                  RedisTemplate<String, Object> redis) {
        this.repo  = repo;
        this.redis = redis;
    }

    public void persistAndCache(ChatMessage cm) {
        // 1) MySQL
        ChatMessageEntity e = new ChatMessageEntity(
                cm.getId(), cm.getPlatform(), cm.getChannel(),
                cm.getUsername(), cm.getMessage(), cm.getTimestamp()
        );
        repo.save(e);

        // 2) Redis Sorted Set + Hash
        long score = cm.getTimestamp()
                .atZone(java.time.ZoneId.of("UTC"))
                .toInstant().toEpochMilli();

        redis.opsForZSet().add("chat:recent", cm.getId(), score);
        redis.opsForHash().put("chat:msg:" + cm.getId(), "payload", cm);

        // 3) Limpiar >5 min
        long cutoff = Instant.now()
                .minus(5, ChronoUnit.MINUTES)
                .toEpochMilli();
        redis.opsForZSet().removeRangeByScore("chat:recent", 0, cutoff);
    }
}
