package demo.chat.storage.service;

import demo.chat.model.ChatMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ChatConsumerService {

    private final ChatPersistenceService persistence;

    public ChatConsumerService(ChatPersistenceService persistence) {
        this.persistence = persistence;
    }

    @KafkaListener(
            topics = "${kafka.topic.chat:chat-messages}",
            groupId = "chat-storage-group",
            containerFactory = "chatMessageKafkaListenerContainerFactory"
    )
    public void onMessage(ChatMessage cm) {
        persistence.persistAndCache(cm);
        System.out.printf("💾 [Storage] %s|%s: %s%n",
                cm.getPlatform(),
                cm.getUsername(),
                cm.getMessage()
        );
    }
}
