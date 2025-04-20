package com.genius.ai;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OllamaService {

    private final WebClient webClient = WebClient.create("http://localhost:11434");

    public String askOllama(String error) {
        String prompt = "Voici une erreur rencontrée dans un microservice Java :\n" + error +
                "\nExplique-la clairement et propose une solution.";

        var response = webClient.post()
                .uri("/api/generate")
                .bodyValue("""
                    {
                      "model": "llama3",
                      "prompt": "%s",
                      "stream": false
                    }
                    """.formatted(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}