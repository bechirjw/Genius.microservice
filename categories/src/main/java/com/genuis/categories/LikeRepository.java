package com.genuis.categories;

import com.genuis.categories.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndRessourceId(Long userId, Long ressourceId);
    List<Like> findByUserId(Long userId);
}
