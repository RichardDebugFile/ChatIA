package demo.chat.storage.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity implements Serializable {

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

    public ChatMessageEntity() {}

    public ChatMessageEntity(String id, String platform, String channel,
                             String username, String message, LocalDateTime timestamp) {
        this.id = id;
        this.platform = platform;
        this.channel = channel;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    // getters y setters...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
