package com.genius.events.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "LIKE" ou "DISLIKE"

    private Long utilisateurId;   // ID utilisateur (extrait du token côté Angular)
    private Long evenementId;     // ID événement (passé via bouton Angular)

    private LocalDateTime dateReaction = LocalDateTime.now();
}
