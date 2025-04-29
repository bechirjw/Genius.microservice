package com.genius.events.control;


import com.genius.events.dto.ParticipationDTO;
import com.genius.events.dto.ParticipationDetailsDTO;
import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;
import com.genius.events.repository.EvenementsRepository;
import com.genius.events.repository.ParticipationsRepository;
import com.genius.events.service.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/participations")
public class ParticipationsRestController {
    IParticipationsService participationsService;



    private final EvenementsRepository evenementsRepository;
    @Autowired
    private ParticipationsRepository participationsRepository;
    private final IListeAttenteService listeAttenteService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/retrieve-all-participations")
    public List<Participations> getParticipations() {
        List<Participations> listParticipations = participationsService.retrieveAllParticipations();
        return listParticipations;
    }


    @GetMapping("/retrieve-participation/{participation-id}")
    public Participations retrieveParticipation(@PathVariable("participation-id") Long participationId) {
        Participations participation = participationsService.retrieveParticipation(participationId);
        return participation;
    }

    @PostMapping("/add-participation")
    public Evenements addParticipation(@RequestBody ParticipationDTO dto) {
        Long idEvent = dto.getEvenementId();

        Evenements event = evenementsRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        // 🚨 Vérifier que les données utilisateur et événement sont présentes
        if (dto.getNomUtilisateur() == null || dto.getNomUtilisateur().isBlank()) {
            throw new RuntimeException("Nom d'utilisateur est vide. Impossible de générer QR code.");
        }

        if (event.getTitre() == null || event.getTitre().isBlank()) {
            throw new RuntimeException("Titre d'événement est vide. Impossible de générer QR code.");
        }

        // 🚨 NOUVEAU : Vérifier que l'utilisateur ne participe pas deux fois
        boolean dejaParticipe = participationsRepository.existsByUtilisateurIdAndEvenementId(
                dto.getUtilisateurId(), dto.getEvenementId());

        if (dejaParticipe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vous avez déjà participé à cet événement !");
        }

        // ✅ Continuer l'ajout normal
        Participations participation = new Participations();
        participation.setEvenement(event);
        participation.setStatut(dto.getStatut());
        participation.setDateInscription(LocalDateTime.now());
        participation.setUtilisateurId(dto.getUtilisateurId());
        participation.setNomUtilisateur(dto.getNomUtilisateur());
        participation.setEmailUtilisateur(dto.getEmailUtilisateur());

        participationsRepository.save(participation);

        // ➡️ Générer le contenu QR
        String qrContent = "Utilisateur: " + dto.getNomUtilisateur() + "\nÉvénement: " + event.getTitre();
        System.out.println("Contenu QR généré : " + qrContent);

        try {
            byte[] qrCodeImage = QRCodeService.generateQRCode(qrContent, 250, 250);

            Path path = Path.of("qrcodes", "qr_" + dto.getNomUtilisateur().replace(" ", "_") + "_" + event.getTitre().replace(" ", "_") + ".png");
            Files.createDirectories(path.getParent());
            Files.write(path, qrCodeImage);

            emailService.sendEmailWithQRCode(
                    dto.getEmailUtilisateur(),
                    qrCodeImage,
                    "Confirmation de votre participation",
                    "Merci pour votre participation à l'événement : " + event.getTitre()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }



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



    @GetMapping("/evenement/{evenementId}/participations-details")
    public ResponseEntity<List<ParticipationDetailsDTO>> getParticipationsDetails(@PathVariable Long evenementId) {
        List<ParticipationDetailsDTO> participations = participationsService.getParticipationDetailsByEvenement(evenementId);
        return ResponseEntity.ok(participations);
    }


}
