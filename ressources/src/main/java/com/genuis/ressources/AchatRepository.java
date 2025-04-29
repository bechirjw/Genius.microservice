package com.genuis.ressources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchatRepository extends JpaRepository<Achat, Long> {
    List<Achat> findByUtilisateurId(Long utilisateurId);
    // Compter le nombre d'achats pour une ressource spécifique
    @Query("SELECT COUNT(a) FROM Achat a WHERE a.ressource.id = :ressourceId")
    Long countAchatByRessourceId(Long ressourceId);
}
