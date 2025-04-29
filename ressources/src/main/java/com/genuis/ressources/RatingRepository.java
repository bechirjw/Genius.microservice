package com.genuis.ressources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByResourceId(Long resourceId);
    long countByResourceIdAndCommentIsNotNull(Long resourceId);
    // Obtenir la moyenne des évaluations pour une ressource spécifique
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.resourceId = :resourceId")
    Double findAverageRatingByResourceId(Long resourceId);
}
