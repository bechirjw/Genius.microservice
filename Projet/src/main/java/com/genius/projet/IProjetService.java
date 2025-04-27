package com.genius.projet;



import java.util.List;

public interface IProjetService {
    List<Projet> retrieveAllProjets();
    Projet retrieveProjet(Long projetId);
    Projet addProjet(Projet p);
    void removeProjet(Long projetId);
    Projet modifyProjet(Projet projet);
    FullProjetResponse findProjetsWithCollaborations(Long projetId);
}
