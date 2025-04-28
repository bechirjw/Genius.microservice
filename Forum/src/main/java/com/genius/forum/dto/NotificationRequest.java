package com.genius.forum.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    private Long receiverId;
    private String type;
    private Long postId; // Car toi c'est un post, pas un projet
}
