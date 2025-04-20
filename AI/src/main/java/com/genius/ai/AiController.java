package com.genius.ai;
import com.genius.ai.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/response")
    public Map<String, String> getAIResponse(@RequestBody Map<String, String> body) {
        String errorMessage = body.get("error");
        String response = ollamaService.askOllama(errorMessage);
        return Map.of("response", response);
    }
}