package com.genius.comment;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/comments")

public class CommentController {
    @Autowired
    private CommentService commentService;
    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String FROM_PHONE = System.getenv("TWILIO_FROM_PHONE");
    private static final String TO_PHONE = System.getenv("TWILIO_TO_PHONE");

    // Récupérer tous les commentaires
    @GetMapping
    public List<Comment> getComments() {
        return commentService.retrieveAllComments();
    }

    // Récupérer un commentaire par ID
    @GetMapping("/{commentId}")
    public Comment retrieveComment(@PathVariable Long commentId) {
        return commentService.retrieveComment(commentId);
    }

    // Ajouter un commentaire
  //  @PostMapping
  //  public ResponseEntity <Comment> addComment(@RequestBody Comment comment) {
      //  Comment createdComment = commentService.addComment(comment);
    //    return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    //}

    // Supprimer un commentaire par ID
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable Long commentId) {
        commentService.removeComment(commentId);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                new PhoneNumber(TO_PHONE),
                new PhoneNumber(FROM_PHONE),
                "Blog deleted: " + commentId
        ).create();
    }

    // Modifier un commentaire existant
    //@PutMapping
  //  public Comment modifyComment(@RequestBody Comment comment) {
     //   return commentService.modifyComment(comment);
    //}




    @PostMapping("/{postId}/{userId}")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment,
                                              @PathVariable Long postId,
                                              @PathVariable Long userId) {
        return ResponseEntity.ok(commentService.addComment(comment, postId, userId));
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
    @PutMapping("/{commentId}/user/{userId}")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment,
                                           @PathVariable Long commentId,
                                           @PathVariable Long userId) {

            comment.setIdComment(commentId);
            return ResponseEntity.ok(commentService.modifyComment(comment,userId));

}

}