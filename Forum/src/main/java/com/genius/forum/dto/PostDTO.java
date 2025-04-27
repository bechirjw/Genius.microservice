package com.genius.forum.dto;

import java.time.LocalDateTime;

public class PostDTO {
    private Long id; // ✅ AJOUT ICI

    private String content;
    private Long communityId;  // L'ID de la communauté
    private String imageUrl; // ✅ Ajouter ce champ + getters/setters
    private String userName;  // 👈 Nouveau
    private String userImage; // 👈 Nouveau
    private LocalDateTime createdAt;
    private String videoUrl; // ✅ Ajouter ce champ



// L'ID de l'utilisateur

    // Constructeurs, getters et setters


    public PostDTO(Long id, Long communityId, String content, String imageUrl, String videoUrl, String userName, String userImage, LocalDateTime createdAt) {
        this.id = id;

        this.communityId = communityId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.userName = userName;
        this.userImage = userImage;
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // Ajoute les getters/setters si pas encore faits
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userNamee) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

}
