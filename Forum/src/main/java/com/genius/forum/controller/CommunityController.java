package com.genius.forum.controller;

import com.genius.forum.dto.CommunityWithPostsDTO;
import com.genius.forum.dto.PostDTO;
import com.genius.forum.model.*;
import com.genius.forum.repository.*;
import com.genius.forum.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private FavoriServiceImpl favoriService;





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

    @PostMapping("/user/{userId}")
    public Community createCommunity(@RequestBody Community community,@PathVariable Long userId) {
        community.setUserId(userId);
        return communityService.createCommunity(community,userId);
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

    /*@PostMapping("/{id}/post/{userId}")
    public Post createPost(@PathVariable Long id, @RequestBody PostDTO postDTO,@PathVariable Long userId) {
        // Assigner l'ID de la communauté et le userId
        postDTO.setCommunityId(id); // Utilisation de l'ID de la communauté depuis l'URL
        postDTO.setUserId(userId); // Utilisation de l'ID de la communauté depuis l'URL
       // postDTO.setUserId(1L); // Par exemple, l'ID de l'utilisateur, tu peux le modifier en fonction de la session ou autre

        // Appeler la méthode createPost avec le DTO
        return postService.createPost(postDTO,userId);
    }*/
    @PostMapping("/{id}/post/{userId}")
    public Post createPost(@PathVariable Long id, @RequestBody PostDTO postDTO, @PathVariable Long userId) {
        postDTO.setCommunityId(id);
        postDTO.setUserId(userId);
        return postService.createPost(postDTO, userId);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id); // Appelle le service pour supprimer le post et envoyer l'email


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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        communityService.deleteCommunity(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    @PostMapping("/posts/{postId}/report")
    public ResponseEntity<Void> reportPost(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setReported(true); // Marque le post comme signalé
        postRepository.save(post);

        return ResponseEntity.ok().build(); // Réponse vide en cas de succès
    }
    // CommunityController.java
    @GetMapping("/posts/reported")
    public ResponseEntity<List<Post>> getReportedPosts() {
        List<Post> reportedPosts = postRepository.findByIsReportedTrue(); // Récupère tous les posts signalés
        return ResponseEntity.ok(reportedPosts);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Community>> getCommunitiesByUser(@PathVariable Long userId) {
        // Appel du service pour récupérer les communautés de l'utilisateur
        List<Community> communities = communityService.getCommunitiesByUser(userId);
        return ResponseEntity.ok(communities);
    }

    // ➕ Ajouter une communauté aux favoris
    @PostMapping("/favoris/add")
    public ResponseEntity<Favori> addFavori(@RequestParam Long userId, @RequestParam Long communityId) {
        Favori favori = favoriService.addFavori(userId, communityId);
        return ResponseEntity.ok(favori);
    }

    // 📥 Récupérer les communautés favorites d'un utilisateur
    @GetMapping("/favoris/user/{userId}")
    public ResponseEntity<List<Community>> getFavorisByUser(@PathVariable Long userId) {
        List<Favori> favoris = favoriService.getFavorisByUserId(userId);
        // Extraire uniquement les communautés
        List<Community> communities = favoris.stream()
                .map(Favori::getCommunity)
                .toList();
        return ResponseEntity.ok(communities);
    }

    @PostMapping("/{postId}/upvote")
    public ResponseEntity<Void> upvote(@PathVariable Long postId, @RequestParam Long userId) {
        postService.vote(postId, userId, Vote.VoteType.UPVOTE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/downvote")
    public ResponseEntity<Void> downvote(@PathVariable Long postId, @RequestParam Long userId) {
        postService.vote(postId, userId, Vote.VoteType.DOWNVOTE);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/upvotes")
    public int getUpvotes(@PathVariable Long postId) {
        return postService.getUpvotes(postId);
    }

    @GetMapping("/{postId}/downvotes")
    public int getDownvotes(@PathVariable Long postId) {
        return postService.getDownvotes(postId);
    }
}








