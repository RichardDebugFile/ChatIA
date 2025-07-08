package demo.chat.service;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import demo.chat.model.ChatMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class TwitchChatService {

    private final KafkaTemplate<String, ChatMessage> kafka;
    // Credenciales hard-codeadas
    private final String clientId     = "6faqdgqqn6c67cirmravgukoo40ygs";
    private final String clientSecret = "aaeiobxy4sq40qyxqdu3vcb8f393ls";
    private final String oauthToken   = "dwg3rhgkaa1lgcf2dcand6ays17454";
    private final String channelName  = "richardxr04";
    private final String topic        = "chat-messages";

    public TwitchChatService(KafkaTemplate<String, ChatMessage> kafka) {
        this.kafka = kafka;
    }

    @PostConstruct
    public void connect() {
        TwitchClient client = TwitchClientBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withChatAccount(new OAuth2Credential("twitch", oauthToken))
                .withEnableChat(true)
                .build();

        client.getChat().joinChannel(channelName);

        client.getEventManager().onEvent(ChannelMessageEvent.class, e -> {
            ChatMessage cm = new ChatMessage(
                    "tw-" + e.getMessageEvent().getMessageId(),
                    "twitch",
                    channelName,
                    e.getUser().getName(),
                    e.getMessage(),
                    LocalDateTime.now()
            );
            kafka.send(topic, cm.getId(), cm);
            System.out.printf("🎮 [Twitch] %s: %s%n",
                    cm.getUsername(), cm.getMessage());
        });

        System.out.println("✅ TwitchChatService conectado a canal: " + channelName);
    }
}
