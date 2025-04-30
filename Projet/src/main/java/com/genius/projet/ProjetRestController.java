package com.genius.projet;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Tag(name = "Gestion Projet")
@RestController
@AllArgsConstructor
@RequestMapping("/projet")

//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class ProjetRestController {

    @Autowired
     IProjetService projetService;
    @Autowired
    private IAService iaService;
    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private TacheRepository tacheRepository;

//    @Operation(description = "Récupérer tous les projets")
    @GetMapping("/retrieve-all-projets")
    public List<Projet> getProjets() {
        return projetService.retrieveAllProjets();
    }

//    @Operation(description = "Récupérer un projet par ID")
    @GetMapping("/retrieve-projet/{projet-id}")
    public Projet retrieveProjet(@PathVariable("projet-id") Long projetId) {
        return projetService.retrieveProjet(projetId);
    }

//    @Operation(description = "Ajouter un projet")
    @PostMapping("/add-projet")
    public Projet addProjet(@RequestBody Projet p) {
        return projetService.addProjet(p);
    }

//    @Operation(description = "Supprimer un projet")
    @DeleteMapping("/remove-projet/{projet-id}")
    public void removeProjet(@PathVariable("projet-id") Long projetId) {
        projetService.removeProjet(projetId);
    }

//    @Operation(description = "Modifier un projet")
    @PutMapping("/modify-projet")
    public Projet modifyProjet(@RequestBody Projet p) {
        return projetService.modifyProjet(p);
    }


    @GetMapping("/with-collaborations/{projet-id}")
    public ResponseEntity<?> findAllProjets(@PathVariable("projet-id") Long projetId) {
        try {
            FullProjetResponse response = projetService.findProjetsWithCollaborations(projetId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 Print error to console
            return ResponseEntity.status(500).body("Erreur interne: " + e.getMessage());
        }
    }





    @PostMapping("/roadmap/{projetId}")
    public Mono<List<Map<String, Object>>> generateRoadmap(@PathVariable("projet-id") Long projetId) {
        Projet projet = projetRepository.findById(projetId).orElseThrow();
        List<String> taches = tacheRepository.findByProjetId(projetId)
                .stream()
                .map(Tache::getTitre)
                .collect(Collectors.toList());

        return iaService.generateRoadmap(projet.getDescription(), taches);
    }


}
