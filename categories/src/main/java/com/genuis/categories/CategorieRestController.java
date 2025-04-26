//http://localhost:8089/backend/swagger-ui/index.html#/Gestion%20des%20Ressources/getRessources
package com.genuis.categories;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Base64;



@Tag(name = "Gestion des Catégories")
@RestController
@AllArgsConstructor
@RequestMapping("/categorie")

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})


public class CategorieRestController {

    @Autowired
    private ICategorieService categorieService;
    @Autowired
    private final FavoriService favoriService;


@Operation(description = "Récupérer toutes les catégories")
@GetMapping("/retrieve-all-categories")
public List<CategorieDTO> getCategories() {
    List<Categorie> categories = categorieService.retrieveAllCategories();
    return categories.stream().map(this::convertToDto).collect(Collectors.toList());
}

private CategorieDTO convertToDto(Categorie categorie) {
    CategorieDTO dto = new CategorieDTO();
    dto.setIdCategorie(categorie.getIdCategorie());
    dto.setNomCategorie(categorie.getNomCategorie());
    dto.setDomaine(categorie.getDomaine());
    dto.setDescription(categorie.getDescription());
    dto.setDateCreation(categorie.getDateCreation());
    dto.setLikes(categorie.getLikes());
    if (categorie.getImage() != null) {
        dto.setImage(Base64.getEncoder().encodeToString(categorie.getImage()));
    }
    return dto;
}
    //@Operation(description = "Récupérer toutes les catégories")
    //@GetMapping("/retrieve-all-categories")
    //public List<Categorie> getCategories() {
     //   return categorieService.retrieveAllCategories();
   // }
    @Operation(description = "Modifier une catégorie existante avec une image (ID dans l'URL)")
    @PutMapping(value = "/modify-categorie/{idCategorie}", consumes = "multipart/form-data")
    @CacheEvict(value = "categoriesCache", allEntries = true)
    public ResponseEntity<Categorie> modifyCategorieWithImage(
            @PathVariable("idCategorie") Long idCategorie,
            @RequestParam("nomCategorie") String nomCategorie,
            @RequestParam("domaine") String domaine,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            // Récupérer la catégorie existante
            Categorie categorie = categorieService.retrieveCategorie(idCategorie);
            if (categorie == null) {
                return ResponseEntity.notFound().build();
            }

            // Mise à jour des champs texte
            categorie.setNomCategorie(nomCategorie);
            categorie.setDomaine(domaine);
            categorie.setDescription(description);

            // Mise à jour de l’image si fournie
            if (image != null && !image.isEmpty()) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                byte[] imageBytes = Files.readAllBytes(filePath);
                categorie.setImage(imageBytes);
            }

            // Enregistrement
            Categorie updatedCategorie = categorieService.modifyCategorie(categorie);
            return ResponseEntity.ok(updatedCategorie);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    @Operation(description = "Récupérer une catégorie par son ID")
    @GetMapping("/retrieve-categorie/{categorie-id}")
    public Categorie retrieveCategorie(@PathVariable("categorie-id") Long idCategorie) {
        return categorieService.retrieveCategorie(idCategorie);
    }

    @Operation(description = "Ajouter une nouvelle catégorie")
    @PostMapping("/add-categorie")
    public Categorie addCategorie(@RequestBody Categorie categorie) {
        return categorieService.addCategorie(categorie);
    }

    @Operation(description = "Supprimer une catégorie par son ID")
    @DeleteMapping("/remove-categorie/{categorie-id}")
    public void removeCategorie(@PathVariable("categorie-id") Long idCategorie) {
        categorieService.removeCategorie(idCategorie);
    }



    @Operation(description = "Mettre à jour le nombre de likes d'une catégorie")
    @PutMapping("/update-likes/{categorie-id}")
    public ResponseEntity<Categorie> updateLikes(@PathVariable("categorie-id") Long idCategorie,
                                                 @RequestParam("likes") Integer likes) {
        Categorie updatedCategorie = categorieService.updateLikes(idCategorie, likes);
        return ResponseEntity.ok(updatedCategorie);
    }
    @Operation(description = "Récupérer une catégorie par son ID avec son image encodée en Base64")
    @GetMapping("/retrieve-categorie-with-image/{categorie-id}")
    public ResponseEntity<CategorieDTO> retrieveCategorieWithImage(@PathVariable("categorie-id") Long idCategorie) {
        Categorie categorie = categorieService.retrieveCategorie(idCategorie);

        if (categorie == null) {
            return ResponseEntity.notFound().build();
        }

        CategorieDTO dto = convertToDto(categorie);
        return ResponseEntity.ok(dto);
    }



    @Operation(description = "Ajouter une catégorie avec une image")
    @PostMapping(value = "/add-categorie-with-image", consumes = "multipart/form-data")
    @CacheEvict(value = "categoriesCache", allEntries = true) // Vide le cache des catégories après l'ajout
    public ResponseEntity<Categorie> addCategorieWithImage(
            @RequestParam("nomCategorie") String nomCategorie,
            @RequestParam("domaine") String domaine,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {
        try {
            // Vérifier si le fichier n'est pas vide
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // Sauvegarde de l'image (Exemple: en base64 ou dans un dossier)
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Lire le fichier image en bytes
            byte[] imageBytes = Files.readAllBytes(filePath);

            // Ajouter les bytes de l'image à la catégorie
            Categorie categorie = new Categorie();
            categorie.setNomCategorie(nomCategorie);
            categorie.setDomaine(domaine);
            categorie.setDescription(description);
            categorie.setImage(imageBytes);  // Stocker les bytes de l’image

            // Sauvegarde en base de données
            categorieService.addCategorie(categorie);

            return ResponseEntity.ok(categorie);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    // Récupérer toutes les ressources avec une catégorie spécifique
    @Operation(description = "Récupérer toutes les ressources avec catégorie")
    @GetMapping("/{categorie-id}")
    public ResponseEntity<FullResources> getRessourcesByCategorie(@PathVariable("categorie-id") Long idCategorie) {
        return ResponseEntity.ok(categorieService.retrieveCategoriewithresources(idCategorie));
    }

    @PostMapping("/api/favoris/ajouter")
    public ResponseEntity<Void> ajouterFavori(@RequestParam Long idUser, @RequestParam Long idCategorie) {
        favoriService.ajouterFavori(idUser, idCategorie);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/favoris/retirer")
    public ResponseEntity<Void> retirerFavori(@RequestParam Long idUser, @RequestParam Long idCategorie) {
        favoriService.retirerFavori(idUser, idCategorie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/favoris/utilisateur/{idUser}")
    public ResponseEntity<List<Categorie>> getFavoris(@PathVariable Long idUser) {
        List<Categorie> favoris = favoriService.getFavorisByUser(idUser);
        return ResponseEntity.ok(favoris);
    }

    // Like une catégorie
    @PostMapping("/{categorieId}/like")
    public void likeCategorie(@PathVariable Long categorieId, @RequestParam Long userId) {
        categorieService.likeCategorie(categorieId, userId);
    }
}
