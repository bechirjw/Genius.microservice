package com.genius.forum.service;

import com.genius.forum.dto.CommunityWithPostsDTO;
import com.genius.forum.model.Community;
import com.genius.forum.model.User;

import java.util.List;

public interface CommunityService {
    List<Community> getAllCommunities();
    Community createCommunity(Community community);
    Community getCommunityById(Long id);
    void joinCommunity(Long communityId, User user);
    CommunityWithPostsDTO getCommunityWithPosts(Long communityId);
    void deleteCommunity(Long communityId);  // ✅ Ajout de la méthode de suppression

}
