package com.genuis.ressources;

import com.genuis.ressources.Ressource;

import java.util.List;

public interface IRessourceService {
    List<Ressource> retrieveAllRessources();

    Ressource retrieveRessource(Long idRessource);

    Ressource addRessource(Ressource ressource);

    void removeRessource(Long idRessource);

    Ressource modifyRessource(Ressource ressource);
}