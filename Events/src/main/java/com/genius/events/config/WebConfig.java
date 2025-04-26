package com.genius.events.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Dossier d'uploads situé à la racine du projet
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        System.out.println("✅ Upload path: " + uploadPath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + uploadPath + "/");
    }


}
