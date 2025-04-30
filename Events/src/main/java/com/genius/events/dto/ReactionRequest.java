package com.genius.events.dto;

import lombok.Data;

@Data
public class ReactionRequest {
    private Long utilisateurId;
    private Long evenementId;
    private String type; // "LIKE" ou "DISLIKE"
}
