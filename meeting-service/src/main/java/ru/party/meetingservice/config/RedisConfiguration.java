package ru.party.meetingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.party.meetingservice.dto.MeetingEventTO;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, MeetingEventTO> redisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, MeetingEventTO> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(MeetingEventTO.class));
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
