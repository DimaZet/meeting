package ru.party.searchservice.config;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.party.searchservice.dto.MeetingEventTO;
import ru.party.searchservice.service.MessageSubscriber;

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

    @Bean
    ChannelTopic topic(@Value("${spring.redis.topic}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            MessageSubscriber messageSubscriber,
            RedisConnectionFactory connectionFactory,
            ChannelTopic topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.addMessageListener(new MessageListenerAdapter(messageSubscriber), topic);
        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
