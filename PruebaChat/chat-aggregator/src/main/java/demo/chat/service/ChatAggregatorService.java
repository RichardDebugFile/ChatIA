package demo.chat.service;

import demo.chat.model.ChatMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ChatAggregatorService {

    private final SimpMessagingTemplate ws;
    private final ConcurrentHashMap<String, AtomicLong> counter = new ConcurrentHashMap<>();

    public ChatAggregatorService(SimpMessagingTemplate ws) {
        this.ws = ws;
    }

    @KafkaListener(topics = "${kafka.topic.chat:chat-messages}")
    public void consume(ChatMessage m) {
        // 1) incremento de contador
        counter.computeIfAbsent(m.getPlatform(), k -> new AtomicLong())
                .incrementAndGet();

        // 2) reenvío vía WebSocket
        ws.convertAndSend("/topic/chat", m);
        ws.convertAndSend("/topic/chat/" + m.getPlatform(), m);

        // 3) LOG en consola
        System.out.printf("🔄 [Aggregator] %s | %s: %s%n",
                m.getPlatform(), m.getUsername(), m.getMessage());
    }
}

