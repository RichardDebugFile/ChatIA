package demo.chat.moderator.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {
    @Id
    @Column(length = 255)
    private String id;

    @Column(length = 50)
    private String platform;

    @Column(length = 255)
    private String channel;

    @Column(length = 255)
    private String username;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    protected ChatMessageEntity() { }

    public ChatMessageEntity(String id, String platform, String channel,
                             String username, String message, LocalDateTime timestamp) {
        this.id         = id;
        this.platform   = platform;
        this.channel    = channel;
        this.username   = username;
        this.message    = message;
        this.timestamp  = timestamp;
    }

    // getters...
    public String getId() { return id; }
    public String getPlatform() { return platform; }
    public String getChannel() { return channel; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
