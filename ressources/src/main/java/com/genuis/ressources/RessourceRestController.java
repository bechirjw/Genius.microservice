package com.genuis.ressources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Tag(name = "Gestion des Ressources")
@RestController
@AllArgsConstructor
@RequestMapping("/ressources")

//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class RessourceRestController {
    private static final Logger logger = LoggerFactory.getLogger(RessourceRestController.class);
    @Autowired
    private IRessourceService ressourceService;
    //  @PostMapping("/{idCategorie}/ajout-ressource/user/{idUser}")
    // public ResponseEntity<?> addRessourceWithFiles(@PathParam("idUser") Long idUser,
    @PostMapping("/{idCategorie}/ajout-ressource")
    public ResponseEntity<?> addRessourceWithFiles(//@PathParam("idUser") Long idUser,
                                                   @PathVariable("idCategorie") Long idCategorie,
                                                   @RequestParam("titre") String titre,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("status") StatutRessource status,
                                                   @RequestParam("type") TypeRessource type,

                                                   @RequestParam(value = "prix", required = false) Long prix,
                                                   @RequestParam(value = "image", required = false) MultipartFile image,
                                                   @RequestParam(value = "files", required = false) MultipartFile[] files,
                                                   @RequestParam(value = "text", required = false) String text,
                                                   @RequestParam(value = "lien", required = false) String lien
    ) {
        try {
            Ressource ressource = new Ressource();

            ressource.setTitre(titre);
            ressource.setDescription(description);
            ressource.setIdCategorie(idCategorie);
            ressource.setType(type);
            //  ressource.setIduser(idUser);


            // Définir le statut
            try {
                ressource.setStatut(status);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Statut invalide. Utilisez 'GRATUIT' ou 'PAYANT'.");
            }

            // Champs optionnels
            ressource.setText(text);
            ressource.setLien(lien);

            if (prix != null) {
                ressource.setPrix(prix);
            }

            if (image != null && !image.isEmpty()) {
                ressource.setImage(image.getBytes());
            }

            List<Fichier> fichiersList = new ArrayList<>();

            if (files != null && files.length > 0) {
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                        String filePath = uploadPath.resolve(System.currentTimeMillis() + "_" + originalFilename).toString();

                        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

                        Fichier fichier = new Fichier();
                        fichier.setNom(originalFilename);
                        fichier.setFilePath(filePath);
                        fichier.setRessource(ressource); // Association inverse
                        fichiersList.add(fichier);
                    }
                }

                ressource.setFichiers(fichiersList);
            }

            // Ressource saved = ressourceService.addRessource(idUser,ressource);
            Ressource saved = ressourceService.addRessource(ressource);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la ressource avec fichiers : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }
    @Operation(description = "Modifier une ressource existante avec fichiers (ID dans l'URL)")
    @PutMapping(value = "/modify-ressource/{idRessource}", consumes = "multipart/form-data")
    public ResponseEntity<?> modifyRessourceWithFiles(
            @PathVariable("idRessource") Long idRessource,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("status") StatutRessource status,
            @RequestParam("type") TypeRessource type,

            @RequestParam(value = "prix", required = false) Long prix,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "lien", required = false) String lien
    ) {
        try {
            // Récupérer la ressource existante
            Ressource ressource = ressourceService.retrieveRessource(idRessource);
            if (ressource == null) {
                return ResponseEntity.notFound().build();
            }

            // Mettre à jour les champs
            ressource.setTitre(titre);
            ressource.setType(type);
            ressource.setDescription(description);
            ressource.setStatut(status);
            ressource.setText(text);
            ressource.setLien(lien);

            if (prix != null) {
                ressource.setPrix(prix);
            }

            // Mise à jour de l’image si fournie
            if (image != null && !image.isEmpty()) {
                ressource.setImage(image.getBytes());
            }

            // Mise à jour des fichiers si fournis
            if (files != null && files.length > 0) {
                List<Fichier> fichiersList = new ArrayList<>();
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                        String filePath = uploadPath.resolve(System.currentTimeMillis() + "_" + originalFilename).toString();

                        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

                        Fichier fichier = new Fichier();
                        fichier.setNom(originalFilename);
                        fichier.setFilePath(filePath);
                        fichier.setRessource(ressource);
                        fichiersList.add(fichier);
                    }
                }

                // Écraser les anciens fichiers (optionnel : on pourrait aussi les garder)
                ressource.setFichiers(fichiersList);
            }

            // Enregistrement de la ressource modifiée
            Ressource updated = ressourceService.modifyRessource(ressource);
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            logger.error("Erreur lors de la modification de la ressource : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }

    // recuperer une ressource par son ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRessourceById(@PathVariable("id") Long id) {
        Optional<Ressource> optionalRessource = ressourceService.getRessourceById(id);

        if (optionalRessource.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ressource non trouvée");
        }

        Ressource ressource = optionalRessource.get();

        // Convertir image en Base64
        String imageBase64 = (ressource.getImage() != null)
                ? Base64.getEncoder().encodeToString(ressource.getImage())
                : null;

        // Mapper les fichiers en DTOs
        List<FichierDTO> fichierDTOs = ressource.getFichiers().stream()
                .map(f -> new FichierDTO(f.getNom(), f.getFilePath()))
                .toList();

        // Créer le DTO de réponse
        com.genuis.ressources.RessourceResponseDTO dto = new com.genuis.ressources.RessourceResponseDTO();
        dto.setIdCategorie(ressource.getIdCategorie());
        dto.setId(ressource.getIdRessource());
        dto.setTitre(ressource.getTitre());
        dto.setDescription(ressource.getDescription());
        dto.setStatut(ressource.getStatut()); // enum (sera converti en "Gratuit"/"Payant" automatiquement)
        dto.setPrix(ressource.getPrix());
        dto.setText(ressource.getText());
        dto.setLien(ressource.getLien());

        dto.setType(ressource.getType() != null ? ressource.getType().name() : null);
        dto.setDateAjout(ressource.getDateAjout());
        dto.setImageBase64(imageBase64);
        dto.setFichiers(fichierDTOs);

        return ResponseEntity.ok(dto);
    }


    // Récupérer toutes les ressources
    @GetMapping
    public ResponseEntity<List<RessourceResponseDTO>> getAllRessources() {
        List<Ressource> ressources = ressourceService.getAllRessources();

        List<RessourceResponseDTO> dtoList = ressources.stream().map(ressource -> {
            // Convertir image en Base64
            String imageBase64 = (ressource.getImage() != null)
                    ? Base64.getEncoder().encodeToString(ressource.getImage())
                    : null;

            // Mapper fichiers
            List<FichierDTO> fichierDTOs = ressource.getFichiers().stream()
                    .map(f -> new FichierDTO(f.getNom(), f.getFilePath()))
                    .toList();

            // Mapper ressource en DTO
            RessourceResponseDTO dto = new RessourceResponseDTO();
            dto.setId(ressource.getIdRessource());
            dto.setTitre(ressource.getTitre());
            dto.setDescription(ressource.getDescription());
            dto.setStatut(ressource.getStatut());
            dto.setPrix(ressource.getPrix());
            dto.setText(ressource.getText());
            dto.setLien(ressource.getLien());

            dto.setType(ressource.getType() != null ? ressource.getType().name() : null);
            dto.setDateAjout(ressource.getDateAjout());
            dto.setImageBase64(imageBase64);
            dto.setFichiers(fichierDTOs);
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }





    // Supprimer une ressource par son ID
    @Operation(description = "Supprimer une ressource par son ID")
    @DeleteMapping("/remove-ressource/{ressource-id}")
    public ResponseEntity<Void> removeRessource(@PathVariable("ressource-id") Long idRessource) {
        ressourceService.removeRessource(idRessource);
        return ResponseEntity.noContent().build();
    }


    // Récupérer toutes les ressources avec une catégorie spécifique
    @Operation(description = "Récupérer toutes les ressources avec catégorie")
    @GetMapping("/categorie/{categorie-id}")
    public ResponseEntity<List<Ressource>> getRessourcesByCategorie(@PathVariable("categorie-id") Long idCategorie) {
        return ResponseEntity.ok(ressourceService.retrieveAllRessourcesByCategories(idCategorie));
    }


}
