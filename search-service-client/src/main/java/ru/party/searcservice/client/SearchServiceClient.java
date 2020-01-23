package ru.party.searcservice.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
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

    public String index(String json) {
        return getResult(
                indexEndpoint,
                HttpMethod.PUT,
                json,
                String.class,
                null
        );
    }

    private <T> T getResult(
            @NonNull String url,
            @NonNull HttpMethod method,
            String json,
            @NonNull Class<T> t,
            @Nullable Map<String, Object> uriVariables) {
        return client.exchange(
                baseUrl + url,
                method,
                new HttpEntity<>(json),
                t,
                uriVariables
        ).getBody();
    }
}
