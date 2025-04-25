package com.genius.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idComment;
    private String description;
    private LocalDateTime CreatedAt = LocalDateTime.now();
    private String CreatedBy;
    private Long postId;
    private Long userId;
    
    private Long likes;

    @ElementCollection
    private Set<Long> likedBy = new HashSet<>();


    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getIdComment() {
        return idComment;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
    }
    public Long getPostId() {
        return postId;
    }
    public Long getUserId() {
        return userId;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }
    public void like(Long userId) {
        if (!likedBy.contains(userId)) {
            this.likes++;
            likedBy.add(userId);
        }
    }
}
