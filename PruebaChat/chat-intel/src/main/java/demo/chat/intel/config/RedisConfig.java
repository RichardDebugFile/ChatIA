package demo.chat.intel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration cfg = new RedisStandaloneConfiguration(host, port);
        // si tu password puede estar vacío, podrías condicionar:
        if (password != null && !password.isBlank()) {
            cfg.setPassword(RedisPassword.of(password));
        }
        return new LettuceConnectionFactory(cfg);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory cf) {
        RedisTemplate<String, Object> tmpl = new RedisTemplate<>();
        tmpl.setConnectionFactory(cf);
        tmpl.setKeySerializer(new StringRedisSerializer());
        tmpl.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return tmpl;
    }
}
