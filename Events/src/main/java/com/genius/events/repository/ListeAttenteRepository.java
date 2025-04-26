package com.genius.events.repository;

import com.genius.events.entity.Evenements;
import com.genius.events.entity.ListeAttente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ListeAttenteRepository extends JpaRepository<ListeAttente, Long> {
    ListeAttente findFirstByEvenementAndNotificationEnvoyeeFalseOrderByDateInscriptionAsc(Evenements evenement);
    boolean existsByEvenementAndEmail(Evenements evenement, String email);

}
