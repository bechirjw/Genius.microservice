package com.genius.forum.repository;

import com.genius.forum.model.Community;
import com.genius.forum.model.Favori;
import com.genius.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriRepository extends JpaRepository<Favori, Long> {
    List<Favori> findByUser(User user);

    Optional<Favori> findByUserAndCommunity(User user, Community community);
}