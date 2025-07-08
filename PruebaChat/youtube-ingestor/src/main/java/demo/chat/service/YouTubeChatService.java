package demo.chat.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveChatMessage;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import demo.chat.model.ChatMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YouTubeChatService {

    private final KafkaTemplate<String, ChatMessage> kafka;
    private final String apiKey       = "";   //Colocar el api propio
    private final String liveChatId   = "";  //Colocar el api propio del chat
    private final String topic        = "chat-messages";
    private YouTube yt;
    private String pageToken = null;

    public YouTubeChatService(KafkaTemplate<String, ChatMessage> kafka) {
        this.kafka = kafka;
    }

    @PostConstruct
    void init() throws Exception {
        // 1) inicialice el cliente de YouTube
        yt = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null)
                .setApplicationName("youtube-ingestor")
                .build();

        // 2) lanza el loop en un hilo aparte
        Thread thread = new Thread(this::runLoop, "youtube-poll-thread");
        thread.setDaemon(true);
        thread.start();
    }

    private void runLoop() {
        while (true) {
            try {
                doPollOnce();
            } catch (Exception e) {
                System.err.println("❌ Error en poll(): " + e.getMessage());
                e.printStackTrace();
                // en error, espera un poco antes de reintentar
                sleepSilently(10_000);
            }
        }
    }

    private void doPollOnce() throws Exception {
        var req = yt.liveChatMessages()
                .list(liveChatId, List.of("snippet","authorDetails"))
                .setKey(apiKey)
                .setMaxResults(200L);
        if (pageToken != null) {
            req.setPageToken(pageToken);
        }

        var resp = req.execute();
        int count = resp.getItems().size();
        System.out.printf("📨 [%s] recibidos: %d mensajes%n",
                LocalDateTime.now(), count);

        for (LiveChatMessage m : resp.getItems()) {
            ChatMessage cm = new ChatMessage(
                    "yt-" + m.getId(),
                    "youtube",
                    liveChatId,
                    m.getAuthorDetails().getDisplayName(),
                    m.getSnippet().getDisplayMessage(),
                    LocalDateTime.now()
            );
            kafka.send(topic, cm.getId(), cm);
            System.out.printf("→ %s: %s%n", cm.getUsername(), cm.getMessage());
        }

        pageToken = resp.getNextPageToken();
        // Respeta el intervalo sugerido por la API:
        long wait = (resp.getPollingIntervalMillis() != null)
                ? resp.getPollingIntervalMillis()
                : 5_000;
        sleepSilently(wait);
    }

    private void sleepSilently(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
