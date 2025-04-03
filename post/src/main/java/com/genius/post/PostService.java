package com.genius.post;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.genius.post.Post;
import com.genius.post.PostRepository;

import java.util.List;

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

    public Post addPost(Post post,Long userId) {
        return postRepository.save(post);
    }

    public void removePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post modifyPost(Post post) {
        return postRepository.save(post);
    }
}