package demo.chat.intel.service;

import demo.chat.intel.model.SummaryResult;
import demo.chat.intel.ollama.PeticionesOllama;
import demo.chat.intel.repo.ChatMessageEntity;
import demo.chat.intel.repo.ChatMessageEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatIntelService {

    private final ChatMessageEntityRepository dbRepo;
    private final RedisTemplate<String,Object> redis;
    private final PeticionesOllama ollama;
    private final String zsetKey, hashPrefix;
    private volatile SummaryResult lastSummary;

    public ChatIntelService(
            ChatMessageEntityRepository dbRepo,
            RedisTemplate<String,Object> redis,
            PeticionesOllama ollama,
            @Value("${chat.redis.recent-key}") String zsetKey,
            @Value("${chat.redis.hash-prefix}") String hashPrefix
    ) {
        this.dbRepo     = dbRepo;
        this.redis      = redis;
        this.ollama     = ollama;
        this.zsetKey    = zsetKey;
        this.hashPrefix = hashPrefix;
    }

    @Scheduled(fixedDelay = 30_000)
    public void summarizeLastFiveMinutes() {
        Instant nowInstant = Instant.now();
        LocalDateTime now   = LocalDateTime.now();
        LocalDateTime cutoff = now.minusMinutes(5);

        // 1) Intento leer de MySQL
        System.out.printf("🔍 Buscando en MySQL entre %s y %s…%n", cutoff, now);
        List<ChatMessageEntity> fromDb = dbRepo.findByTimestampBetween(cutoff, now);
        System.out.printf("✅ Encontrados %d mensajes en MySQL%n", fromDb.size());
        List<String> rawMessages;
        if (!fromDb.isEmpty()) {
            rawMessages = fromDb.stream()
                    .map(e -> String.format("[%s] %s", e.getUsername(), e.getMessage()))
                    .collect(Collectors.toList());
        } else {
            // 2) Fallback a Redis
            long nowMs    = nowInstant.toEpochMilli();
            long cutoffMs = nowMs - 5*60*1000;
            Set<Object> ids = redis.opsForZSet().rangeByScore(zsetKey, cutoffMs, nowMs);
            if (ids == null || ids.isEmpty()) return;
            rawMessages = ids.stream()
                    .map(id -> (String) redis.opsForHash()
                            .get(hashPrefix + id, "payload"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        if (rawMessages.isEmpty()) return;

        String prompt = "Resume estos últimos mensajes en 20 palabras:\n"
                + String.join("\n", rawMessages);

        String summary = ollama.consultar(prompt);
        System.out.printf("⏱ [%s] Resumen IA: %s%n", nowInstant, summary);

        // Guardar el último resumen generado
        lastSummary = new SummaryResult(nowInstant, List.of(), summary);
    }

    public SummaryResult getLastSummary() {
        return lastSummary;
    }
}

