package com.genius.collaboration;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//@Tag(name = "Gestion Collaboration")
@RestController
@AllArgsConstructor
@RequestMapping("/collaboration")

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class CollaborationRestController {

    @Autowired
    ICollaborationService collaborationService;

//    @Operation(description = "Récupérer toutes les collaborations")
    @GetMapping("/retrieve-all-collaborations")
    public List<Collaboration> getCollaborations() {
        return collaborationService.retrieveAllCollaborations();
    }

//    @Operation(description = "Récupérer une collaboration par ID")
    @GetMapping("/retrieve-collaboration/{collaboration-id}")
    public Collaboration retrieveCollaboration(@PathVariable("collaboration-id") Long collaborationId) {
        return collaborationService.retrieveCollaboration(collaborationId);
    }

//    @Operation(description = "Ajouter une collaboration")
    @PostMapping("/add-collaboration")
    public Collaboration addCollaboration(@RequestBody Collaboration c) {
        return collaborationService.addCollaboration(c);
    }

//    @Operation(description = "Supprimer une collaboration")
    @DeleteMapping("/remove-collaboration/{collaboration-id}")
    public void removeCollaboration(@PathVariable("collaboration-id") Long collaborationId) {
        collaborationService.removeCollaboration(collaborationId);
    }

//    @Operation(description = "Modifier une collaboration")
    @PutMapping("/modify-collaboration")
    public Collaboration modifyCollaboration(@RequestBody Collaboration c) {
        return collaborationService.modifyCollaboration(c);
    }

    @GetMapping("/projet/{projet-id}")
    public ResponseEntity<List<Collaboration>> findAllCollaborations(
            @PathVariable("projet-id") Long projetId
    ) {
        return ResponseEntity.ok(collaborationService.findAllCollaborationsByProjet(projetId));
    }
//    @PutMapping("/{id}/accepter")
//    public ResponseEntity<Collaboration> accepterCollaboration(@PathVariable Long id) {
//        Collaboration collab = collaborationService.accepterCollaboration(id);
//        if (collab == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(collab);
//    }
@PutMapping("/collaborations/{id}/accepter") public ResponseEntity<Collaboration> accepterCollaboration(@PathVariable Long id) { Collaboration updated = collaborationService.accepterCollaboration(id); if (updated != null) { return ResponseEntity.ok(updated); } else { return ResponseEntity.notFound().build(); } }


}
