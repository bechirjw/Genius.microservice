package com.genius.forum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
        private String content;
    private LocalDateTime createdAt;
    @Column(length = 1000)
    private String imageUrl;
    private String videoUrl; // ✅ Ajouter ceci


    private String userName;   // 👈 à ajouter
    private String userImage;  // 👈 à ajouter
    private boolean isReported = false;  // Nouveau champ pour signaler un post
    private Long userId;
    private Integer likes =0;

    @ElementCollection
    @CollectionTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "user_id")
    private Set<Long> likedBy = new HashSet<>();

    public void like(Long userId) {
        if (!likedBy.contains(userId)) {
            likes++;
            likedBy.add(userId);
        }
    }

    public boolean hasUserLiked(Long userId) {
        return likedBy.contains(userId);
    }


    public Post(String content) {
        this.content = content;

    }

    @ManyToOne
    private Community community;
}
