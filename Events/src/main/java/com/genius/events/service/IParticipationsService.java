package com.genius.events.service;


import com.genius.events.dto.ParticipationDetailsDTO;
import com.genius.events.dto.ParticipationEventDTO;
import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;

import java.util.List;

public interface IParticipationsService {
    List<Participations> retrieveAllParticipations();
    Participations retrieveParticipation(Long idParticipation);

    void removeParticipation(Long idParticipation);
    Participations modifyParticipation(Participations participation);

    List<Evenements> getEvenementsByUtilisateurId(Long utilisateurId);

    Long countByEvenementId(Long idEvenement);
    List<ParticipationDetailsDTO> getParticipationDetailsByEvenement(Long evenementId);
    Participations addParticipation(Participations participation);
    List<ParticipationEventDTO> getParticipationsOfUtilisateur(Long utilisateurId);

}
