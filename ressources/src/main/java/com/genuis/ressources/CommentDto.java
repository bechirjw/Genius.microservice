package com.genuis.ressources;

public class CommentDto {
    private String username;
    private String comment;

    // Constructeur
    public CommentDto(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    // Getters et setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
