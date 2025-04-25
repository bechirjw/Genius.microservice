package com.genius.forum.dto;

import java.util.List;

public class CommunityWithPostsDTO {

    private Long id; // <-- AJOUTER ÇA

    private String name;
    private String description;
    private List<PostDTO> posts;
    private String imageUrl; // URL de l'image de la communauté


    // Constructeurs, getters et setters

    public CommunityWithPostsDTO(Long id, String name, String description, String imageUrl, List<PostDTO> posts) {
        this.id = id;

        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;

        this.posts = posts;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }
}
