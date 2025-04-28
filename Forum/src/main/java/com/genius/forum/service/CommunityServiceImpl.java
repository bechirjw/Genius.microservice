package com.genius.forum.service;

import com.genius.forum.dto.CommunityWithPostsDTO;
import com.genius.forum.dto.PostDTO;
import com.genius.forum.model.Community;
import com.genius.forum.model.Membership;
import com.genius.forum.model.Post;
import com.genius.forum.model.User;
import com.genius.forum.repository.CommunityRepository;
import com.genius.forum.repository.MembershipRepository;
import com.genius.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    @Override
    public Community createCommunity(Community community) {
        return communityRepository.save(community);
    }

    @Override
    public Community getCommunityById(Long id) {
        return communityRepository.findById(id).orElseThrow();
    }


    @Override
    public void joinCommunity(Long communityId, User user) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        // Vérifier si l'utilisateur fait déjà partie de la communauté
        if (membershipRepository.existsByUserAndCommunity(user, community)) {
            throw new RuntimeException("User already a member of the community");
        }

        Membership membership = new Membership();
        membership.setCommunity(community);
        membership.setUser(user);
        membershipRepository.save(membership);
    }

    @Override

    public CommunityWithPostsDTO getCommunityWithPosts(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        List<Post> posts = postRepository.findByCommunityId(communityId);

        // Transformer les entités en DTO
        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(
                        post.getId(),
                        post.getCommunity().getId(),
                        post.getContent(),
                        post.getImageUrl(),
                        post.getVideoUrl(), // ✅ Ajouter ici

                        "Alice", // ou récupère dynamiquement post.getUser().getName()
                        "https://img.freepik.com/photos-gratuite/jeune-belle-fille-posant-dans-veste-cuir-noire-parc_1153-8104.jpg?semt=ais_hybrid&w=740",
                        post.getCreatedAt(),
                        post.isReported(),
                        post.getUserId()


                ))
                .collect(Collectors.toList());


        // Retourner la communauté avec les posts
        return new CommunityWithPostsDTO(
                community.getId(),
                community.getName(),
                community.getDescription(),
                community.getImageUrl(),
                postDTOs
        );    }

    @Override

    public void deleteCommunity(Long communityId) {
        // Supprimer les posts associés à cette communauté
        List<Post> posts = postRepository.findByCommunityId(communityId);
        postRepository.deleteAll(posts);

        // Supprimer la communauté elle-même
        communityRepository.deleteById(communityId);
    }
}


