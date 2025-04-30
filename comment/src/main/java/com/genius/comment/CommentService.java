package com.genius.comment;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.management.Notification;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_SERVICE_URL = "http://localhost:8222/api/v1/posts/";
    private static final String NOTIFICATION_SERVICE_URL = "http://localhost:5220/api/notifications/send";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired

    private CommentRepository commentRepository;
    @Autowired

    public List<Comment> retrieveAllComments() {
        return commentRepository.findAll();
    }
    public Comment retrieveComment(Long CommentId) {

        return commentRepository.findById(CommentId).orElse(null);
    }
   /* public Comment addComment (Comment comment) {
        return commentRepository.save(comment );
    }*/

    private void sendNotificationToPostOwner(Comment comment) {
        try {
            // 1. Récupération du post
            String url = POST_SERVICE_URL + comment.getPostId();
            Map post = restTemplate.getForObject(url, Map.class);

            if (post == null || post.get("userId") == null) {
                System.err.println("❌ Post not found or invalid for comment ID: " + comment.getIdComment());
                return;
            }

            Long ownerId = Long.valueOf(post.get("userId").toString());

            // 2. Vérifier si l'utilisateur commente son propre post
            if (comment.getUserId().equals(ownerId)) {
                System.out.println("ℹ️ User commented on their own post, no notification sent.");
                return;
            }

            // 3. Préparer la notification
            NotificationRequest notification = new NotificationRequest(
                    "Quelqu'un a commenté votre post !",
                    ownerId,
                    ownerId,
                    "NEW_COMMENT",
                    comment.getPostId()
            );
            notification.setSenderId(comment.getUserId());

            // 4. Créer les headers avec application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 5. Construire la requête HTTP
            HttpEntity<NotificationRequest> request = new HttpEntity<>(notification, headers);

            // 6. Envoyer la notification
            restTemplate.postForObject(NOTIFICATION_SERVICE_URL, request, Void.class);
            System.out.println("✅ Notification envoyée à l'auteur du post (userId=" + ownerId + ")");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de la notification pour commentaire : " + e.getMessage());
        }
    }



    /*public Comment addComment(Comment c) {
        Comment savedComment= commentRepository.save(c);

        // 🔥 After saving collaboration, send notification

        return savedComment;
    }*/
    public Comment addComment(Comment comment, Long postId, Long userId) {
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setCreatedAt(LocalDateTime.now());

        // 🔍 Appliquer le filtre AVANT de sauvegarder
        comment.setDescription(filterBadWords(comment.getDescription()));

        // 💾 Sauvegarder APRES que toutes les modifications soient faites
        Comment savedComment = commentRepository.save(comment);

        // 📢 Envoyer la notification avec des données cohérentes
        sendNotificationToPostOwner(savedComment);

        return savedComment;
    }

    private static final List<String> BAD_WORDS = List.of("merde", "isreal", "con", "putain", "shit");

    private String filterBadWords(String content) {
        try {
            String url = "https://www.purgomalum.com/service/json?text=" +
                    URLEncoder.encode(content, StandardCharsets.UTF_8);

            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                return response.get("result");
            }
        } catch (Exception e) {
            System.err.println("API failed, using local filtering: " + e.getMessage());
        }

        // Fallback local filter
        String filteredContent = content;
        for (String badWord : BAD_WORDS) {
            String stars = "*".repeat(badWord.length());
            filteredContent = filteredContent.replaceAll("(?i)\\b" + Pattern.quote(badWord) + "\\b", stars);
        }
        return filteredContent;
    }


    public Comment modifyComment(Comment newComment, Long userId) {
        Comment existingComment = commentRepository.findById(newComment.getIdComment()).orElse(null);

        if (existingComment == null) {
            throw new RuntimeException("Comment not found");
        }

        if (!existingComment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to modify this comment");
        }

        // 🔥 Appliquer le filtre aussi à la modification
        String filtered = filterBadWords(newComment.getDescription());
        existingComment.setDescription(filtered);

        return commentRepository.save(existingComment);
    }



    public void removeComment (Long CommentId) {
        commentRepository.deleteById(CommentId);
    }




    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
    public void likeComment(Long commentId, Long userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.like(userId);
            commentRepository.save(comment);
        }
    }
    public void deleteCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        commentRepository.deleteAll(comments);
    }


}
