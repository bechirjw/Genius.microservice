package com.genius.projet;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

//@Tag(name = "Gestion Tache")
@RestController
@AllArgsConstructor
@RequestMapping("/tache")

//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class TacheRestController {

    @Autowired
    private  ITacheService tacheService;

    // Récupérer toutes les tâches
    @GetMapping("/retrieve-all-taches")
    public List<Tache> getTaches() {
        return tacheService.retrieveAllTaches();
    }

    // Récupérer une tâche par ID
    @GetMapping("/retrieve-tache/{tache-id}")
    public Tache retrieveTache(@PathVariable("tache-id") Long tacheId) {
        return tacheService.retrieveTache(tacheId);
    }

    // Ajouter une tâche
    @PostMapping("/add-tache/{projet-id}")
    public Tache addTache(@RequestBody Tache t, @PathVariable("projet-id") Long projetId) {
        return tacheService.addTache(t, projetId);
    }

    // Supprimer une tâche
    @DeleteMapping("/remove-tache/{tache-id}")
    public void removeTache(@PathVariable("tache-id") Long tacheId) {
        tacheService.removeTache(tacheId);
    }

    // Modifier une tâche
    @PutMapping("/modify-tache")
    public Tache modifyTache(@RequestBody Tache t) {
        return tacheService.modifyTache(t);
    }

    // Récupérer toutes les tâches d’un projet donné
    @GetMapping("/projet/{projet-id}")
    public List<Tache> getTachesByProjet(@PathVariable("projet-id") Long projetId) {
        return tacheService.getTachesByProjet(projetId);
    }

    @PutMapping("/update-statut/{tache-id}")
    public Tache updateStatut(@PathVariable("tache-id") Long tacheId, @RequestBody Map<String, String> payload) {
        String newStatut = payload.get("statut");
        return tacheService.updateStatut(tacheId, newStatut);
    }

}
