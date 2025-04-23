package com.genius.events.repository;


import com.genius.events.entity.Evenements;
import com.genius.events.entity.StatutEvenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface EvenementsRepository extends JpaRepository<Evenements,Long >{
    @Query("SELECT e FROM Evenements e LEFT JOIN FETCH e.participations WHERE e.id = :id")
    Optional<Evenements> findByIdWithParticipations(@Param("id") Long id);


    List<Evenements> findByStatut(StatutEvenement statut);

}
