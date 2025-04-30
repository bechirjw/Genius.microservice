package com.genius.forum.service;

import com.genius.forum.dto.PostDTO;
import com.genius.forum.model.Community;
import com.genius.forum.model.Post;
import com.genius.forum.model.User;
import com.genius.forum.model.Vote;
import com.genius.forum.repository.CommunityRepository;
import com.genius.forum.repository.PostRepository;
import com.genius.forum.repository.UserRepository;
import com.genius.forum.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private VoteRepository voteRepository;


    @Autowired
    private AIService aiService;



    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Post> getPostsByCommunityId(Long communityId) {
        return postRepository.findByCommunityId(communityId);
    }
    @Override
    public Post createPost(PostDTO postDTO, Long userId) {
        Community community = communityRepository.findById(postDTO.getCommunityId())
                .orElseThrow(() -> new RuntimeException("Community not found"));

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setCommunity(community);
        post.setImageUrl(postDTO.getImageUrl());
        post.setVideoUrl(postDTO.getVideoUrl());
        post.setUserId(userId); // ✅ assigner le vrai userId ici

        return postRepository.save(post);
    }
   /* @Override
    public Post createPost(PostDTO postDTO,Long UserId) {
        Community community = communityRepository.findById(postDTO.getCommunityId())
                .orElseThrow(() -> new RuntimeException("Community not found"));

       // User user = userRepository.findById(postDTO.getUserId())
                //.orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setCommunity(community);
       // post.setUser(user);
        post.setImageUrl(postDTO.getImageUrl()); // <--- Ici
        post.setVideoUrl(postDTO.getVideoUrl());
        post.setUserId(post.getUserId());



        return postRepository.save(post);
    }*/

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + id + " not found"));

        postRepository.deleteById(id);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ahmed.neji03@gmail.com");
        message.setSubject("Votre post a été supprimé car il ne respecte pas les règles de la communauté." );
        message.setText("Votre post a été supprimé car il ne respecte pas les règles de la communauté.");

        mailSender.send(message);
        System.out.println("✅ Mail envoyé");

        // Envoyer la notification à l'auteur
        sendNotificationToPostOwner(post);
    }


    public void generatePostFromCommunityName(String communityName) {
        // Ensure you're passing the community name to the AI API
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("community_name", communityName);  // Ensure community name is passed here

        // Send the POST request to the external AI service (if necessary)
        restTemplate.postForEntity("http://localhost:5000/generate", requestPayload, String.class);
    }
    public void sendNotificationToPostOwner(Post post) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Préparer la notification
            Map<String, Object> notification = new HashMap<>();
            notification.put("message", "Votre post a été supprimé car il ne respecte pas les règles de la communauté.");
            notification.put("receiverId", post.getUserId()); // 🚨 Ton Post doit avoir un getUserId() sinon il faudra l'ajouter
            notification.put("type", "POST_DELETED");
            notification.put("postId", post.getId()); // Tu peux aussi envoyer le postId

            // Envoyer la notification au microservice notification
            restTemplate.postForObject(
                    "http://localhost:5220/api/notifications/send",
                    notification,
                    Map.class
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
        }
    }

    public void vote(Long postId, Long userId, Vote.VoteType type) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Vote> existingVote = voteRepository.findByUserAndPost(user, post);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (vote.getType() == type) {
                // Supprimer le vote si l'utilisateur clique une deuxième fois sur le même vote
                voteRepository.delete(vote);
            } else {
                vote.setType(type);
                voteRepository.save(vote);
            }
        } else {
            Vote newVote = new Vote();
            newVote.setUser(user);
            newVote.setPost(post);
            newVote.setType(type);
            voteRepository.save(newVote);
        }
    }

    public int getUpvotes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return voteRepository.countByPostAndType(post, Vote.VoteType.UPVOTE);
    }

    public int getDownvotes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return voteRepository.countByPostAndType(post, Vote.VoteType.DOWNVOTE);
    }
}












