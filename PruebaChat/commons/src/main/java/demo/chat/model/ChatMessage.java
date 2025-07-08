package demo.chat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ChatMessage implements Serializable {

    private String id;
    private String platform;  // "twitch" | "youtube"
    private String channel;
    private String username;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(String id, String platform, String channel,
                       String username, String message, LocalDateTime timestamp) {
        this.id = id;
        this.platform = platform;
        this.channel = channel;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    /* getters & setters */
    public String getId()                   { return id; }
    public void setId(String id)            { this.id = id; }
    public String getPlatform()             { return platform; }
    public void setPlatform(String p)       { this.platform = p; }
    public String getChannel()              { return channel; }
    public void setChannel(String c)        { this.channel = c; }
    public String getUsername()             { return username; }
    public void setUsername(String u)       { this.username = u; }
    public String getMessage()              { return message; }
    public void setMessage(String m)        { this.message = m; }
    public LocalDateTime getTimestamp()     { return timestamp; }
    public void setTimestamp(LocalDateTime t){ this.timestamp = t; }

    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ChatMessage cm)) return false;
        return Objects.equals(id, cm.id);
    }
    @Override public int hashCode(){ return Objects.hash(id); }
}
