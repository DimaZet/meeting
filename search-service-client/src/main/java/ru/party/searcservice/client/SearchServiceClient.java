package ru.party.searcservice.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.party.searcservice.client.dto.MeetingEventTO;

@Component
public class SearchServiceClient {

    private final String baseUrl;
    private final String indexEndpoint;
    private final RestTemplate client = new RestTemplate();

    @Autowired
    public SearchServiceClient(
            @Value("{searchservice.api.url}") String baseUrl,
            @Value("{searchservice.api.index}") String indexEndpoint) {
        this.baseUrl = baseUrl;
        this.indexEndpoint = indexEndpoint;
    }

    public MeetingEventTO index(MeetingEventTO eventTO) {
        return getResult(
                indexEndpoint,
                HttpMethod.PUT,
                MeetingEventTO.class,
                null
        );
    }

    private <T> T getResult(
            @NonNull String url,
            @NonNull HttpMethod method,
            @NonNull Class<T> t,
            @Nullable Map<String, Object> uriVariables) {
        return client.exchange(
                baseUrl + url,
                method,
                HttpEntity.EMPTY,
                t,
                uriVariables
        ).getBody();
    }
}
