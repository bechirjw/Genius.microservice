//http://localhost:8089/backend/swagger-ui/index.html#/Gestion%20des%20Ressources/getRessources
package com.genuis.categories;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "Gestion des Catégories")
@RestController
@AllArgsConstructor
@RequestMapping("/categorie")

public class CategorieRestController {

    @Autowired
    private ICategorieService categorieService;

    @Operation(description = "Récupérer toutes les catégories")
    @GetMapping("/retrieve-all-categories")
    public List<Categorie> getCategories() {
        return categorieService.retrieveAllCategories();
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

    @Operation(description = "Modifier une catégorie existante")
    @PutMapping("/modify-categorie")
    public Categorie modifyCategorie(@RequestBody Categorie categorie) {
        return categorieService.modifyCategorie(categorie);
    }

    @Operation(description = "Récupérer une catégorie par son ID")
    @GetMapping("/with-resources/{categorie-id}")
    public ResponseEntity<FullResources> getCategories(@PathVariable("categorie-id") Long idCategorie) {
        FullResources resources = categorieService.retrieveCategoriewithresources(idCategorie);
        return ResponseEntity.ok(resources);
    }

}
