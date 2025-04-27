package com.genius.forum.service;

import com.genius.forum.dto.PostDTO;
import com.genius.forum.model.Community;
import com.genius.forum.model.Post;
import com.genius.forum.repository.CommunityRepository;
import com.genius.forum.repository.PostRepository;
import com.genius.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RestTemplate restTemplate;



    @Autowired
    private CommunityRepository communityRepository;


    @Autowired
    private AIService aiService;



    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Post> getPostsByCommunityId(Long communityId) {
        return postRepository.findByCommunityId(communityId);
    }

    @Override
    public Post createPost(PostDTO postDTO) {
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



        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Post with id " + id + " not found");
        }
    }

    public void generatePostFromCommunityName(String communityName) {
        // Ensure you're passing the community name to the AI API
        Map<String, String> requestPayload = new HashMap<>();
        requestPayload.put("community_name", communityName);  // Ensure community name is passed here

        // Send the POST request to the external AI service (if necessary)
        restTemplate.postForEntity("http://localhost:5000/generate", requestPayload, String.class);
    }




}




