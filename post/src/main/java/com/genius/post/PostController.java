package com.genius.post;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestion des Posts")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    @Autowired
    private  PostService postService;

    // Récupérer tous les posts
    @GetMapping

    public List<Post> getPosts() {
        return postService.retrieveAllPosts();
    }

    // Récupérer un post par ID
    @GetMapping("/{post-id}")
    public Post retrievePost(@PathVariable("post-id") Long postId) {
        return postService.retrievePost(postId);
    }

    // Ajouter un post
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> addPost(@RequestBody Post post, @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(postService.addPost(post,userId));
    }

    // Supprimer un post par ID
    @DeleteMapping("/{post-id}")
    public void removePost(@PathVariable("post-id") Long postId) {
        postService.removePost(postId);
    }

    // Modifier un post existant
    @PutMapping("/modify-post")
    public Post modifyPost(@RequestBody Post post) {
        return postService.modifyPost(post);
    }
}