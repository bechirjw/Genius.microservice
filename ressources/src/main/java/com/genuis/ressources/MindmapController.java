package com.genuis.ressources;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/mindmap")
public class MindmapController {

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateMindmap(@RequestParam("filepath") String filepath) {
        try {
            // Décodage et normalisation du chemin
            String decodedPath = URLDecoder.decode(filepath, StandardCharsets.UTF_8);
            decodedPath = decodedPath.replace("/", "\\"); // pour Windows

            File pdfFile = new File(decodedPath);

            if (!pdfFile.exists()) {
                String msg = "Fichier introuvable : " + decodedPath;
                System.err.println(msg);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(msg.getBytes());
            }

            // Lancer le script Python
            ProcessBuilder builder = new ProcessBuilder(
                    "python",
                    "D:\\Pi-dev\\gitquiz\\Genius.microservice\\ressources\\scripts\\generate_mindmap.py",
                    pdfFile.getAbsolutePath()
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Lire la sortie du script
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Script output:\n" + output);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String msg = "Erreur dans le script Python. Code de sortie : " + exitCode + "\nSortie : " + output;
                System.err.println(msg);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(msg.getBytes());
            }

            // Charger l'image générée
            File imageFile = new File("mindmap_color.png");
            if (!imageFile.exists()) {
                String msg = "Image générée non trouvée : mindmap_color.png";
                System.err.println(msg);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(msg.getBytes());
            }

            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"mindmap.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Erreur serveur : " + e.getClass().getSimpleName() + " - " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(msg.getBytes());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadMindmap() {
        try {
            // Chemin vers le fichier PNG généré par le script Python
            File imageFile = new File("mindmap_color.png");
            if (!imageFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("Fichier de mindmap non trouvé.").getBytes());
            }

            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"mindmap.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
