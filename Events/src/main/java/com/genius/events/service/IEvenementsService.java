package com.genius.events.service;



import com.genius.events.entity.Evenements;
import com.genius.events.entity.StatutEvenement;

import java.util.List;

public interface IEvenementsService {
    List<Evenements> retrieveAllEvenements();
    Evenements retrieveEvenement(Long idEvenement);
    Evenements addEvenement(Evenements evenement);
    void removeEvenement(Long idEvenement);
    Evenements modifyEvenement(Evenements evenement);
    List<Evenements> getEvenementsByStatut(StatutEvenement statut);
    List<Evenements> getEvenementsByUtilisateur(Long utilisateurId);

}
