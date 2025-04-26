package com.genius.events.service;


import com.genius.events.dto.ParticipationDetailsDTO;
import com.genius.events.dto.UserDTO;
import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;
import com.genius.events.repository.ParticipationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ParitipationsServiceImpl implements IParticipationsService {

    ParticipationsRepository participationsRepository;

    private final UserClient userClient; // injection

    public List<Participations> retrieveAllParticipations() {
        return participationsRepository.findAll();
    }


    public Participations retrieveParticipation(Long idParticipation) {
        return participationsRepository.findById(idParticipation).get();
    }


    public Participations addParticipation(Participations participation) {
        return participationsRepository.save(participation);
    }


    public void removeParticipation(Long idParticipation) {
        participationsRepository.deleteById(idParticipation);
    }


    public Participations modifyParticipation(Participations participation) {
        return participationsRepository.save(participation);
    }


    @Override
    public List<Evenements> getEvenementsByUtilisateurId(Long utilisateurId) {
        return participationsRepository.findEvenementsByUtilisateurId(utilisateurId);
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
            dto.setStatut(participation.getStatut());
            dto.setUtilisateurId(participation.getUtilisateurId());

            try {
                // 🎯 Appel réel au user-service
                UserDTO user = userClient.getUserById(participation.getUtilisateurId());
                dto.setNomUtilisateur(user.getName());
                dto.setEmailUtilisateur(user.getEmail());
            } catch (Exception e) {
                // 🛡️ Si le service user est down, fallback mock
                dto.setNomUtilisateur("Utilisateur Test");
                dto.setEmailUtilisateur("test@example.com");
            }

            detailsList.add(dto);
        }

        return detailsList;
    }


}
