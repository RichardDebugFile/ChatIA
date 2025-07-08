package demo.chat.moderator.model;

import java.time.LocalDateTime;
import java.util.List;

public class ModerationResult {
    private LocalDateTime lastCheck;
    private int messagesScanned;
    private List<Violation> violations;
    private boolean hasViolations;

    public ModerationResult() {}

    public ModerationResult(LocalDateTime lastCheck, int messagesScanned, List<Violation> violations) {
        this.lastCheck = lastCheck;
        this.messagesScanned = messagesScanned;
        this.violations = violations;
        this.hasViolations = violations != null && !violations.isEmpty();
    }

    // Getters y Setters
    public LocalDateTime getLastCheck() { return lastCheck; }
    public void setLastCheck(LocalDateTime lastCheck) { this.lastCheck = lastCheck; }

    public int getMessagesScanned() { return messagesScanned; }
    public void setMessagesScanned(int messagesScanned) { this.messagesScanned = messagesScanned; }

    public List<Violation> getViolations() { return violations; }
    public void setViolations(List<Violation> violations) { 
        this.violations = violations; 
        this.hasViolations = violations != null && !violations.isEmpty();
    }

    public boolean isHasViolations() { return hasViolations; }
    public void setHasViolations(boolean hasViolations) { this.hasViolations = hasViolations; }

    public static class Violation {
        private String username;
        private String message;
        private LocalDateTime timestamp;
        private String platform;

        public Violation() {}

        public Violation(String username, String message, LocalDateTime timestamp, String platform) {
            this.username = username;
            this.message = message;
            this.timestamp = timestamp;
            this.platform = platform;
        }

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }
    }
} 