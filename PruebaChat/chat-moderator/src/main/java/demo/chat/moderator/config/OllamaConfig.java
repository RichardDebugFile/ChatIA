package demo.chat.moderator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {
    @Value("${ollama.url}")
    private String url;

    @Value("${ollama.model}")
    private String model;

    public String getUrl() {
        return url;
    }

    public String getModel() {
        return model;
    }
}
