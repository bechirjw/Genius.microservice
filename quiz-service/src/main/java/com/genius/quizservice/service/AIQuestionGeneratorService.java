package com.genius.quizservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIQuestionGeneratorService {

    @Value("${python.api.url}")
    private String pythonApiUrl;

    private final RestTemplate restTemplate;

    public AIQuestionGeneratorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateQuestions(String description) {
        try {
            String requestBody = String.format("{\"description\":\"%s\"}", description);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    pythonApiUrl + "/generate-questions",
                    entity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            return String.format("{\"error\":\"%s\"}", e.getMessage());
        }
    }
}