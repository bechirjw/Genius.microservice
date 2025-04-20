package com.genuis.ressources;

// PythonRunner.java


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonRunner {

    public static String runMindmapGenerator(String pdfPath) throws IOException, InterruptedException {
        // Chemin absolu vers le script Python (à adapter selon votre déploiement)
        String pythonScriptPath = "scripts/mindmap_generator.py";

        ProcessBuilder pb = new ProcessBuilder(
                "python3",
                pythonScriptPath,
                pdfPath  // Chemin du PDF passé en argument
        );

        Process process = pb.start();

        // Lire la sortie JSON
        StringBuilder jsonOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonOutput.append(line);
            }
        }

        // Attendre la fin du processus
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Erreur Python (code " + exitCode + ")");
        }

        return jsonOutput.toString();
    }
}