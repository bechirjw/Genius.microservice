package com.genuis.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public Like likeRessource(Long userId, Long ressourceId) {
        Optional<Like> existing = likeRepository.findByUserIdAndRessourceId(userId, ressourceId);
        if (existing.isPresent()) {
            return existing.get(); // déjà liké
        }
        Like like = new Like();
        like.setUserId(userId);
        like.setRessourceId(ressourceId);
        return likeRepository.save(like);
    }

    public void unlikeRessource(Long userId, Long ressourceId) {
        Optional<Like> existing = likeRepository.findByUserIdAndRessourceId(userId, ressourceId);
        existing.ifPresent(likeRepository::delete);
    }

    public List<Like> getLikesByUser(Long userId) {
        return likeRepository.findByUserId(userId);
    }
}

