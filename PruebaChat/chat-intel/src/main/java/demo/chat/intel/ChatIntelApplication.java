package demo.chat.intel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatIntelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatIntelApplication.class, args);
    }
}
