package com.genius.events.service;


import com.genius.events.dto.ParticipationDTO;
import com.genius.events.dto.ParticipationDetailsDTO;
import com.genius.events.dto.ParticipationEventDTO;
import com.genius.events.dto.UserDTO;
import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;
import com.genius.events.repository.EvenementsRepository;
import com.genius.events.repository.ParticipationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ParitipationsServiceImpl implements IParticipationsService {

    ParticipationsRepository participationsRepository;

    private final UserClient userClient; // injection
    @Autowired
    private EvenementsRepository evenementsRepository;
    public List<Participations> retrieveAllParticipations() {
        return participationsRepository.findAll();
    }


    public Participations retrieveParticipation(Long idParticipation) {
        return participationsRepository.findById(idParticipation).get();
    }
    @Override
    public Participations addParticipation(Participations participation) {
        return participationsRepository.save(participation);
    }


    @PostMapping("/add-participation")
    public Evenements addParticipation(@RequestBody ParticipationDTO dto) {
        Long idEvent = dto.getEvenementId();
        Evenements event = evenementsRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        Participations participation = new Participations();
        participation.setEvenement(event);
        participation.setStatut(dto.getStatut());
        participation.setDateInscription(LocalDateTime.now());
        participation.setUtilisateurId(dto.getUtilisateurId());
        participation.setNomUtilisateur(dto.getNomUtilisateur());
        participation.setEmailUtilisateur(dto.getEmailUtilisateur());

        participationsRepository.save(participation);

        // 🔥 Génération QR Code
        String qrContent = "Utilisateur: " + dto.getNomUtilisateur() + "\nÉvénement: " + event.getTitre();
        try {
            byte[] qrCodeImage = QRCodeService.generateQRCode(qrContent, 250, 250);

            // 1. Sauvegarder fichier (optionnel)
            Path path = Path.of("qrcodes", "qr_" + dto.getNomUtilisateur() + "_" + event.getTitre() + ".png");
            Files.createDirectories(path.getParent());
            Files.write(path, qrCodeImage);

            // 2. (Option) Envoyer par email


        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }
    @Override
    public List<Evenements> getEvenementsByUtilisateurId(Long utilisateurId) {
        return participationsRepository.findEvenementsByUtilisateurId(utilisateurId);
    }

    @Override
    public List<ParticipationEventDTO> getParticipationsOfUtilisateur(Long utilisateurId) {
        return participationsRepository.findParticipationsByUtilisateurId(utilisateurId);
    }


    public void removeParticipation(Long idParticipation) {
        participationsRepository.deleteById(idParticipation);
    }


    public Participations modifyParticipation(Participations participation) {
        return participationsRepository.save(participation);
    }





    public Long countByEvenementId(Long idEvenement) {
        return participationsRepository.countByEvenementId(idEvenement);
    }



    @Override
    public List<ParticipationDetailsDTO> getParticipationDetailsByEvenement(Long evenementId) {
        List<Participations> participations = participationsRepository.findByEvenementId(evenementId);
        List<ParticipationDetailsDTO> detailsList = new ArrayList<>();

        for (Participations participation : participations) {
            ParticipationDetailsDTO dto = new ParticipationDetailsDTO();
            dto.setEvenementId(participation.getEvenement().getId());
            dto.setStatut(participation.getStatut().name());
            dto.setUtilisateurId(participation.getUtilisateurId());

            // 🔥 Utiliser directement ce qui est stocké dans Participation
            dto.setNomUtilisateur(participation.getNomUtilisateur());
            dto.setEmailUtilisateur(participation.getEmailUtilisateur());

            detailsList.add(dto);
        }

        return detailsList;
    }





}
