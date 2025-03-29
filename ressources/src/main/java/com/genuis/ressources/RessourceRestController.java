//http://localhost:5010/swagger-ui/index.html#/Gestion%20des%20Ressources/getRessources
package com.genuis.ressources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.genuis.ressources.Ressource;
import com.genuis.ressources.IRessourceService;
import java.util.List;

@Tag(name = "Gestion des Ressources")
@RestController
@AllArgsConstructor
@RequestMapping("/ressources")

public class RessourceRestController {
    @Autowired
    private IRessourceService ressourceService;

    // http://localhost:5010/ressources/retrieve-all-ressources
    @Operation(description = "Récupérer toutes les ressources")
    @GetMapping("/retrieve-all-ressources")
    public ResponseEntity<List<Ressource>> getRessources() {
        return ResponseEntity.ok(ressourceService.retrieveAllRessources());
    }

    // http://localhost:5010/ressources/retrieve-ressource/{ressource-id}
    @Operation(description = "Récupérer une ressource par son ID")
    @GetMapping("/retrieve-ressource/{ressource-id}")
    public ResponseEntity<Ressource> retrieveRessource(@PathVariable("ressource-id") Long idRessource) {
        Ressource ressource = ressourceService.retrieveRessource(idRessource);
        return ResponseEntity.ok(ressource);
    }

    // http://localhost:5010/ressources/add-ressources
    @Operation(description = "Ajouter une nouvelle ressource")
    @PostMapping("/add-ressources")
    public ResponseEntity<Ressource> addRessource(@RequestBody Ressource ressource) {
        Ressource newRessource = ressourceService.addRessource(ressource);
        return ResponseEntity.ok(newRessource);
    }

    // http://localhost:5010/ressources/remove-ressources/{ressource-id}
    @Operation(description = "Supprimer une ressource par son ID")
    @DeleteMapping("/remove-ressources/{ressource-id}")
    public ResponseEntity<Void> removeRessource(@PathVariable("ressource-id") Long idRessource) {
        ressourceService.removeRessource(idRessource);
        return ResponseEntity.noContent().build();
    }

    // http://localhost:5010/ressources/modify-ressources
    @Operation(description = "Modifier une ressource existante")
    @PutMapping("/modify-ressources")
    public ResponseEntity<Ressource> modifyRessource(@RequestBody Ressource ressource) {
        Ressource updatedRessource = ressourceService.modifyRessource(ressource);
        return ResponseEntity.ok(updatedRessource);
    }

    @Operation(description = "Récupérer toutes les ressources with categorie")
    @GetMapping("/categorie/{categorie-id}")
    public ResponseEntity<List<Ressource>> getRessources(@PathVariable("categorie-id") Long idCategorie) {
        return ResponseEntity.ok(ressourceService.retrieveAllRessourcesByCategories(idCategorie));
    }
}