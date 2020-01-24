package ru.party.meetingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.party.meetingservice.dto.MeetingEventTO;

@Service
public class RedisMessagePublisher implements MessagePublisher {

    private final RedisTemplate<String, MeetingEventTO> redisTemplate;
    private final String topic;

    @Autowired
    public RedisMessagePublisher(
            RedisTemplate<String, MeetingEventTO> redisTemplate,
            @Value("${spring.redis.topic}") String topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(Object message) {
        redisTemplate.convertAndSend(topic, message);
    }
}
