package com.genuis.ressources;



import java.util.List;
import java.util.Optional;

public interface IRessourceService {


    Ressource retrieveRessource(Long idRessource);

    Ressource addRessource(Ressource ressource);

    void removeRessource(Long idRessource);

    Ressource modifyRessource(Ressource ressource);
    List<Ressource> getAllRessources();

    List<Ressource> retrieveAllRessourcesByCategories(Long idCategorie);

    Optional<Ressource>  getRessourceById(Long id);
}