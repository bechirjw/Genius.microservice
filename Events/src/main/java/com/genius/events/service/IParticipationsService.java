package com.genius.events.service;


import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;

import java.util.List;

public interface IParticipationsService {
    List<Participations> retrieveAllParticipations();
    Participations retrieveParticipation(Long idParticipation);
    Participations addParticipation(Participations participation);
    void removeParticipation(Long idParticipation);
    Participations modifyParticipation(Participations participation);

    List<Evenements> getEvenementsByUtilisateurId(Long utilisateurId);

    Long countByEvenementId(Long idEvenement);

}
