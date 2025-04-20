package com.genius.post;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;
    private static final String ACCOUNT_SID = "AC3e8ff4c6ec792ca180ab9523cb41234d";
    private static final String AUTH_TOKEN = "2f4bbbbe65e865340606a0c7535c5dd7";
    private static final String FROM_PHONE = "+17622142590";
    private static final String TO_PHONE = "+21658978570";

    private static final String UPLOAD_DIR = "post/uploads/";

    // Récupérer tous les posts
    @GetMapping
    public List<Post> getPosts() {
        return postService.retrieveAllPosts();
    }


    // Récupérer un post par ID
    @GetMapping("/{postId}")
    public Post retrievePost(@PathVariable Long postId) {
        return postService.retrievePost(postId);
    }

    // Ajouter un post sans utiliser userId dans l'URL
    @PostMapping()
    public ResponseEntity<Post> addPost(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            @RequestParam("userId") Long userId,
            @RequestParam("createdBy") String createdBy) {
        try {
            String imageUrl = saveImage(image);
            Post post = new Post();
            post.setTitle(title);
            post.setContent(description);
            post.setImageUrl(imageUrl);
            post.setUserId(userId);
            post.setCreatedBy(createdBy);
            return ResponseEntity.ok(postService.addPost(post));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        image.transferTo(filePath.toFile());
        return fileName;
    }

    // Supprimer un post par ID
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePost(@PathVariable Long postId) {
        // Récupérer le post avant suppression pour avoir les détails
        Post postToDelete = postService.retrievePost(postId);

        postService.removePost(postId);

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                new PhoneNumber(TO_PHONE),
                new PhoneNumber(FROM_PHONE),
                "📌 [Blog Admin] Genius sSuppression d'article\n" +
 "• Titre: \"" + postToDelete.getTitle() + "\"\n" +
"• Supprimé le: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM à HH'h'mm")) + "Cette action est irréversible."
        ).create();
    }


    // Modifier un post existant
    @PutMapping("/{id}/user/{userId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @PathVariable Long userId,@RequestBody Post post) {
        post.setId(id);

        return ResponseEntity.ok(postService.modifyPost(post,userId));
    }
    //facebook
   /* @PostMapping("/facebook/post")
    public ResponseEntity<String> postToFacebook(@RequestBody String message) {
        try {
            postService.postToFacebook(message);
            return ResponseEntity.ok("Message publié avec succès sur Facebook !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }*/


}