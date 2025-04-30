package com.genius.forum.service;

import com.genius.forum.dto.PostDTO;
import com.genius.forum.model.Post;
import com.genius.forum.model.Vote;

import java.util.List;

public interface PostService {
    List<Post> getPostsByCommunityId(Long communityId);
    //Post createPost(Long communityId, Long userId, String content);


    Post createPost(PostDTO postDTO,Long userId);
    void deletePost(Long id);  // ✅ Ajout de la méthode de suppression

    void generatePostFromCommunityName(String communityName);

    void sendNotificationToPostOwner(Post post);
    // Récupérer tous les posts pour une communauté donnée

    void vote(Long postId, Long userId, Vote.VoteType type);
    int getUpvotes(Long postId);
    int getDownvotes(Long postId);

}
