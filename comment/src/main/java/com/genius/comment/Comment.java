package com.genius.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idComment;
    @Lob
    @Column(columnDefinition = "LONGTEXT")

    private String description;

    private LocalDateTime CreatedAt = LocalDateTime.now();
    private String CreatedBy;
    private Long postId;
    private Long userId;

    private Long likes = 0L;  // Initialize to 0 if no likes have been set

    @ElementCollection
    @JsonIgnore // Ignore the 'likedBy' set from being serialized into JSON
    private Set<Long> likedBy = new HashSet<>();  // Initialize here as well



    // Getter for likes to return the count
    public Long getLikes() {
        return likes;
    }

    // Logic for liking a comment
    public void like(Long userId) {
        if (likedBy == null) {
            likedBy = new HashSet<>();  // Initialize likedBy if null
        }

        if (!likedBy.contains(userId)) {
            likes++;  // Increment likes count
            likedBy.add(userId);  // Add user to the likedBy set
        }
    }


}
