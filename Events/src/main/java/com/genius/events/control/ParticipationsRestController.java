package com.genius.events.control;


import com.genius.events.dto.ParticipationDTO;
import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;
import com.genius.events.repository.EvenementsRepository;
import com.genius.events.repository.ParticipationsRepository;
import com.genius.events.service.IListeAttenteService;
import com.genius.events.service.IParticipationsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/participations")
public class ParticipationsRestController {
    IParticipationsService participationsService;

    private final EvenementsRepository evenementsRepository;
    @Autowired
    private ParticipationsRepository participationsRepository;
    private final IListeAttenteService listeAttenteService;

    // http://localhost:8089/backend/participations/retrieve-all-participations
    @GetMapping("/retrieve-all-participations")
    public List<Participations> getParticipations() {
        List<Participations> listParticipations = participationsService.retrieveAllParticipations();
        return listParticipations;
    }

    // http://localhost:8089/backend/participations/retrieve-participation/{participation-id}
    @GetMapping("/retrieve-participation/{participation-id}")
    public Participations retrieveParticipation(@PathVariable("participation-id") Long participationId) {
        Participations participation = participationsService.retrieveParticipation(participationId);
        return participation;
    }

    // http://localhost:8089/backend/participations/add-participation
   // @PostMapping("/add-participation")
  //  public Participations addParticipation(@RequestBody Participations p) {
      //  Participations participation = participationsService.addParticipation(p);
     //   return participation;
  //  }


    @PostMapping("/add-participation")
    public Evenements addParticipation(@RequestBody ParticipationDTO dto) {
        Long idEvent = dto.getEvenementId();

        Evenements event = evenementsRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        Participations participation = new Participations();
        participation.setEvenement(event);
        participation.setStatut(dto.getStatut());
        participation.setDateInscription(LocalDateTime.now());

        // 🟢 N'oublie pas cette ligne :
        participation.setUtilisateurId(dto.getUtilisateurId());

        participationsRepository.save(participation);

        return evenementsRepository.findById(idEvent).orElseThrow();
    }








    // http://localhost:8089/backend/participations/remove-participation/{participation-id}
   // @DeleteMapping("/remove-participation/{participation-id}")
  //  public void removeParticipation(@PathVariable("participation-id") Long participationId) {
      //  participationsService.removeParticipation(participationId);
  //  }

    // http://localhost:8089/backend/participations/modify-participation
    @PutMapping("/modify-participation")
    public Participations modifyParticipation(@RequestBody Participations p) {
        Participations participation = participationsService.modifyParticipation(p);
        return participation;
    }
    @DeleteMapping("/annuler/{idParticipation}")
    public ResponseEntity<Void> annulerParticipation(@PathVariable Long idParticipation) {
        Participations participation = participationsRepository.findById(idParticipation)
                .orElseThrow(() -> new RuntimeException("Participation non trouvée"));

        Evenements evenement = participation.getEvenement();
        participationsRepository.delete(participation);

        // ✅ CETTE LIGNE DOIT ÊTRE PRÉSENTE
        listeAttenteService.notifierPremierEnAttente(evenement);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/evenements-par-utilisateur/{utilisateurId}")
    public ResponseEntity<List<Evenements>> getEvenementsParUtilisateur(@PathVariable Long utilisateurId) {
        List<Evenements> events = participationsService.getEvenementsByUtilisateurId(utilisateurId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/count-by-evenement/{id}")
    public Long countByEvenement(@PathVariable Long id) {
        return participationsService.countByEvenementId(id);
    }


}
