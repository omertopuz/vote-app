package org.example.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.adapters.outbound.api.ExternalServiceAccessImpl;
import org.example.adapters.outbound.event.EventPublisherImpl;
import org.example.adapters.outbound.persistence.VotePersistenceImpl;
import org.example.domain.service.ScoreService;
import org.example.domain.service.VoteService;
import org.example.ports.inbounds.EventListen;
import org.example.ports.inbounds.ScoreUseCase;
import org.example.ports.inbounds.VoteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Vote API", version = "v1", description = "This is a REST API to send vote and show results"),
        tags = {
                @Tag(name = "votes", description = "It creates a new voting")
                ,@Tag(name = "scores", description = "It shows score for the election given")
        }
)
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final VotePersistenceImpl votePersistence;

    @Bean
    public VoteUseCase buildVoteUseCase(){
        return new VoteService(votePersistence,new ExternalServiceAccessImpl(),new EventPublisherImpl());
    }

    @Bean
    public ScoreUseCase buildScoreUseCase(){
        return new ScoreService(new ExternalServiceAccessImpl(),votePersistence);
    }

    @Bean
    public EventListen buildVoteEventListen(){
        return new ScoreService(new ExternalServiceAccessImpl(), votePersistence);
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
