package com.genuis.ressources;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchatRepository extends JpaRepository<Achat, Long> {
    List<Achat> findByUtilisateurId(Long utilisateurId);
}
