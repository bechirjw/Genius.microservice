/* package com.genius.post;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Plus général que /api/v1/**
                .allowedOrigins(
                        "http://localhost:4200",  // Angular en dev
                        "https://votre-domaine.com"  // Votre domaine en prod
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Ajout d'OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // Cache les pré-vérifications CORS pendant 1 heure
    }
}*/