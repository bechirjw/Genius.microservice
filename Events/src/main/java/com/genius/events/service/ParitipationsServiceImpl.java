package com.genius.events.service;


import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;
import com.genius.events.repository.ParticipationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class ParitipationsServiceImpl implements IParticipationsService  {

     ParticipationsRepository participationsRepository;


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




}
