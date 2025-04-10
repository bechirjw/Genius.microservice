package com.genius.projet;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//@Tag(name = "Gestion Projet")
@RestController
@AllArgsConstructor
@RequestMapping("/projet")

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class ProjetRestController {

    @Autowired
     IProjetService projetService;

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
    public ResponseEntity<FullProjetResponse> findAllProjets(
            @PathVariable("projet-id") Long projetId
    ) {
        return ResponseEntity.ok(projetService.findProjetsWithCollaborations(projetId));
    }

}
