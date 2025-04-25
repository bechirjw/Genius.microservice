package com.genius.projet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IAService {

    private final WebClient webClient;

    public IAService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:5001") // Adjust this URL if needed
                .build();
    }

    public Mono<List<Map<String, Object>>> generateRoadmap(String description, List<String> taches) {
        RoadmapRequest request = new RoadmapRequest();
        request.setDescription(description);
        request.setTaches(taches);


        return webClient.post()
                .uri("/generate-roadmap")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });
    }
}
