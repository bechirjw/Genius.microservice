package com.genius.forum.controller;

import com.genius.forum.dto.CommunityWithPostsDTO;
import com.genius.forum.dto.PostDTO;
import com.genius.forum.model.*;
import com.genius.forum.repository.*;
import com.genius.forum.service.AIService;
import com.genius.forum.service.CommunityService;
import com.genius.forum.service.PostService;
import com.genius.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/communities")
@CrossOrigin(origins = "http://localhost:4200")
public class CommunityController {

    private final AIService AIService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    public CommunityController(AIService aiService, CommunityRepository communityRepository) {
        this.AIService = aiService;
        this.communityRepository = communityRepository;
    }

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityWithPostsDTO> getCommunityWithPosts(@PathVariable Long communityId) {
        CommunityWithPostsDTO communityWithPosts = communityService.getCommunityWithPosts(communityId);
        return ResponseEntity.ok(communityWithPosts);
    }

    @GetMapping
    public List<Community> getCommunities() {
        return communityService.getAllCommunities();
    }

    @PostMapping
    public Community createCommunity(@RequestBody Community community) {
        return communityService.createCommunity(community);
    }

    @PostMapping("/{id}/join")
    public void joinCommunity(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        communityService.joinCommunity(id, user);
    }

    @GetMapping("/{id}/posts")
    public List<Post> getPosts(@PathVariable Long id) {
        return postService.getPostsByCommunityId(id);
    }

    @PostMapping("/{id}/post")
    public Post createPost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        // Assigner l'ID de la communauté et le userId
        postDTO.setCommunityId(id); // Utilisation de l'ID de la communauté depuis l'URL
       // postDTO.setUserId(1L); // Par exemple, l'ID de l'utilisateur, tu peux le modifier en fonction de la session ou autre

        // Appeler la méthode createPost avec le DTO
        return postService.createPost(postDTO);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


    @PostMapping("/generate/{communityId}")
    public ResponseEntity<?> generatePost(@PathVariable Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        String communityName = community.getName();

        // Appel au service AI pour générer le contenu du post
        Map<String, String> aiResponse = AIService.generatePostFromCommunityName(communityName);

        String generatedContent = aiResponse.get("post"); // on suppose que le champ est "post"

        // Création du post et sauvegarde en base
        Post post = new Post();
        post.setContent(generatedContent);
        post.setCommunity(community);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        return ResponseEntity.ok(post);
    }




}