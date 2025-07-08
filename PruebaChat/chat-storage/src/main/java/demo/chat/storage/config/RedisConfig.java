package demo.chat.storage.config;

import org.springframework.beans.factory.annotation.Value;                // <<<<< AÑADIDO
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")                                           // <<< USA @Value para leer del yml
    private String host;

    @Value("${redis.port}")                                           // <<< USA @Value
    private int port;

    @Value("${redis.password}")                                       // <<< USA @Value
    private String password;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration cfg = new RedisStandaloneConfiguration(host, port);
        cfg.setPassword(password);
        return new LettuceConnectionFactory(cfg);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            LettuceConnectionFactory cf) {
        RedisTemplate<String, Object> tmpl = new RedisTemplate<>();
        tmpl.setConnectionFactory(cf);
        tmpl.setKeySerializer(new StringRedisSerializer());
        tmpl.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return tmpl;
    }
}
