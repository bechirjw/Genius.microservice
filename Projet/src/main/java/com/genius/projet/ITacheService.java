package com.genius.projet;

import java.util.List;

public interface ITacheService {
    List<Tache> retrieveAllTaches();

    Tache retrieveTache(Long id);

    Tache addTache(Tache t, Long projetId);

    void removeTache(Long id);

    Tache modifyTache(Tache t);

    List<Tache> getTachesByProjet(Long projetId);

}
