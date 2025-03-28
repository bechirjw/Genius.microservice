package com.genuis.ressources;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.genuis.ressources.Ressource;
import com.genuis.ressources.RessourceRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RessourceServiceImpl implements IRessourceService {
    @Autowired
    private RessourceRepository ressourceRepository;

    @Override
    public List<Ressource> retrieveAllRessources() {
        return ressourceRepository.findAll();
    }

    @Override
    public Ressource retrieveRessource(Long idRessource) {
        return ressourceRepository.findById(idRessource).orElse(null);
    }

    @Override
    public Ressource addRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @Override
    public void removeRessource(Long idRessource) {
        ressourceRepository.deleteById(idRessource);
    }

    @Override
    public Ressource modifyRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }
}