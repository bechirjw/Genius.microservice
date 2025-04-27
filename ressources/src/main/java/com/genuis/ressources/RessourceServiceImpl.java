package com.genuis.ressources;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RessourceServiceImpl implements IRessourceService {
    @Autowired
    private RessourceRepository ressourceRepository;
    @Override
    public Optional<Ressource> getRessourceById(Long id) {
        return ressourceRepository.findById(id);
    }


    @Override
    public Ressource retrieveRessource(Long idRessource) {
        return ressourceRepository.findById(idRessource).orElse(null);
    }
    @Override
    public Ressource addRessource(Ressource ressource) {
        // Ici, tu enregistres la ressource dans la base de données
        return ressourceRepository.save(ressource);}
    @Override
    public void removeRessource(Long idRessource) {
        ressourceRepository.deleteById(idRessource);
    }
@Override
public List<Ressource> getAllRessources() {
    return ressourceRepository.findAll();
}

    @Override
    public Ressource modifyRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }
    @Override
    public List<Ressource> retrieveAllRessourcesByCategories(Long idCategorie){
        return ressourceRepository.findAllByIdCategorie(idCategorie);
    }

}