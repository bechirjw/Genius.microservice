package com.genius.post;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> retrieveAllPosts() {
        return postRepository.findAll();
    }

    public Post retrievePost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    public void removePost(Long postId) {
        postRepository.deleteById(postId);
    }

   /* public Post modifyPost(Long id,Post post) {
        return postRepository.save(post);
    }*/
    public Post modifyPost(Post newPost, Long userId) {
        Post existingComment = postRepository.findById(newPost.getId()).orElse(null);

        if (existingComment == null) {
            throw new RuntimeException("Post not found");
        }

        if (!existingComment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to modify this Post");
        }



        return postRepository.save(existingComment);
    }
   /* public void postToFacebook(String message) {
        String accessToken = System.getenv("EAAGkSjCTepQBO6ekix7TsFxAGz3Wn2TRrd6qtCLfQHiITZBV59JC6XsEobSa9YaaCHugKgzNvt7f1qKhxfc32jqZBGb9NtbkE3QA7kEp1e55QcZChTX4ziRCsGGlpT3x60ZCJB3cj38zgnAZAyJgmZB7VuXJ7OYQTyr8qXKNqIvxNIydRDbxlnlgOrueOYOkYZD"); // 🔐 Utilise une variable d'env
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);
        facebookClient.publish("me/feed", FacebookType.class, Parameter.with("message", message));
    }*/


}