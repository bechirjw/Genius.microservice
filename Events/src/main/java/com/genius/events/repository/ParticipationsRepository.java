package com.genius.events.repository;


import com.genius.events.dto.ParticipationEventDTO;
import com.genius.events.entity.Evenements;
import com.genius.events.entity.Participations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ParticipationsRepository extends JpaRepository<Participations,Long >{

    @Query("SELECT COUNT(p) FROM Participations p WHERE p.evenement.id = :eventId")
    Long countByEvenementId(@Param("eventId") Long eventId);


    @Query("SELECT p.evenement FROM Participations p WHERE p.utilisateurId = :utilisateurId")
    List<Evenements> findEvenementsByUtilisateurId(@Param("utilisateurId") Long utilisateurId);


    @Query("SELECT p FROM Participations p WHERE p.evenement.id = :evenementId")
    List<Participations> findByEvenementId(@Param("evenementId") Long evenementId);

    @Query("SELECT COUNT(p) > 0 FROM Participations p WHERE p.utilisateurId = :utilisateurId AND p.evenement.id = :evenementId")
    boolean existsByUtilisateurIdAndEvenementId(@Param("utilisateurId") Long utilisateurId, @Param("evenementId") Long evenementId);

    @Query("SELECT new com.genius.events.dto.ParticipationEventDTO(p.id, e.titre, e.lieu, e.dateDebut, e.dateFin) " +
            "FROM Participations p JOIN p.evenement e WHERE p.utilisateurId = :utilisateurId")
    List<ParticipationEventDTO> findParticipationsByUtilisateurId(@Param("utilisateurId") Long utilisateurId);
}
