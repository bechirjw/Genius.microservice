package com.genius.events.service;

import com.genius.events.entity.Evenements;
import com.genius.events.entity.StatutEvenement;
import com.genius.events.repository.EvenementsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class EvenementsServiceImpl implements IEvenementsService {

     EvenementsRepository evenementsRepository;


    public List<Evenements> retrieveAllEvenements() {
        return evenementsRepository.findAll();
    }


    public Evenements retrieveEvenement(Long idEvenement) {
        return evenementsRepository.findById(idEvenement).get() ;
    }


    public Evenements addEvenement(Evenements evenement) {
        return evenementsRepository.save(evenement);
    }


    public void removeEvenement(Long idEvenement) {
        evenementsRepository.deleteById(idEvenement);
    }


    public Evenements modifyEvenement(Evenements evenement) {
        return evenementsRepository.save(evenement);
    }

    @Override
    public List<Evenements> getEvenementsByStatut(StatutEvenement statut) {
        return evenementsRepository.findByStatut(statut);
    }


}
