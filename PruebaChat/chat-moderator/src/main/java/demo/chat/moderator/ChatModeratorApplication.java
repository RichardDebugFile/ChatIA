package demo.chat.moderator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatModeratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatModeratorApplication.class, args);
    }
}
