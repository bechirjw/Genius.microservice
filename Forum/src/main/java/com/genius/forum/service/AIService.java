package com.genius.forum.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    public Map<String, String> generatePostFromCommunityName(String communityName) {
        String url = "http://127.0.0.1:5000/generate";

        RestTemplate restTemplate = new RestTemplate();

        // ✅ ENVOYER automatiquement le nom reçu de la base vers Flask
        Map<String, String> body = new HashMap<>();
        body.put("communityName", communityName); // ⚠️ PAS "keyword" ou autre

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // attend un objet { post: "..." }
        } else {
            throw new RuntimeException("AI generation failed");
        }
    }
}
