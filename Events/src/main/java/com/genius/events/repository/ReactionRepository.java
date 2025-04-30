package com.genius.events.repository;

import com.genius.events.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUtilisateurIdAndEvenementId(Long utilisateurId, Long evenementId);
    long countByEvenementIdAndType(Long evenementId, String type); // pour compter les likes/dislikes
}
