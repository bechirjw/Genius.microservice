package com.genius.comment;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@AllArgsConstructor
public class CommentService {
    @Autowired

    private CommentRepository commentRepository;
    public List<Comment> retrieveAllComments() {
        return commentRepository.findAll();
    }
    public Comment retrieveComment(Long CommentId) {

        return commentRepository.findById(CommentId).orElse(null);
    }
   /* public Comment addComment (Comment comment) {
        return commentRepository.save(comment );
    }*/

   public Comment addComment(Comment comment, Long postId, Long userId) {
       comment.setPostId(postId);
       comment.setUserId(userId);
       comment.setCreatedAt(LocalDateTime.now());

       // 🔍 Appliquer le filtre sur le contenu avant d'enregistrer
       comment.setDescription(filterBadWords(comment.getDescription()));

       return commentRepository.save(comment);
   }
    private static final List<String> BAD_WORDS = List.of("merde", "isreal", "con", "putain", "shit");

    private String filterBadWords(String content) {
        String filteredContent = content;
        for (String badWord : BAD_WORDS) {
            String stars = "*".repeat(badWord.length());
            filteredContent = filteredContent.replaceAll("(?i)\\b" + badWord + "\\b", stars);
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
   // public Comment modifyComment (Comment comment,Long userId) {

     //   return commentRepository.save(comment);
   // }



    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

}
