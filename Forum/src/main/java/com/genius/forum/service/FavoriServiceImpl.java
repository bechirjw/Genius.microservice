package com.genius.forum.service;

import com.genius.forum.model.Community;
import com.genius.forum.model.Favori;
import com.genius.forum.model.User;
import com.genius.forum.repository.CommunityRepository;
import com.genius.forum.repository.FavoriRepository;
import com.genius.forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriServiceImpl implements FavoriService {
    private final FavoriRepository favoriRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    public FavoriServiceImpl(FavoriRepository favoriRepository, UserRepository userRepository, CommunityRepository communityRepository) {
        this.favoriRepository = favoriRepository;
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
    }

    @Override
    public Favori addFavori(Long userId, Long communityId) {
        User user = userRepository.findById(userId).orElseThrow();
        Community community = communityRepository.findById(communityId).orElseThrow();

        favoriRepository.findByUserAndCommunity(user, community).ifPresent(existing -> {
            throw new RuntimeException("Already favorited");
        });

        Favori favori = new Favori(user, community);
        return favoriRepository.save(favori);
    }

    @Override
    public List<Favori> getFavorisByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return favoriRepository.findByUser(user);
    }
}
