package com.genius.forum.service;

import com.genius.forum.model.Favori;
import com.genius.forum.repository.CommunityRepository;
import com.genius.forum.repository.FavoriRepository;
import com.genius.forum.repository.UserRepository;

import java.util.List;

public interface FavoriService {
    Favori addFavori(Long userId, Long communityId);
    List<Favori> getFavorisByUserId(Long userId);
}
