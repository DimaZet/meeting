package ru.party.meeting.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.party.meeting.security.JwtUser;

@Configuration
@EnableMongoRepositories(basePackages = "ru.party.meeting.repository")
@EnableMongoAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(
                ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
        );
    }
}
