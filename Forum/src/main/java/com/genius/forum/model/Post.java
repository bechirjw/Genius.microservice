package com.genius.forum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @ManyToOne
    private Community community;
}
