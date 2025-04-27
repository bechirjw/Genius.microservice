package com.genuis.ressources;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId; // juste l'ID de l'utilisateur

    @ManyToOne
    private Ressource ressource; // la ressource achetée

    private String dateAchat;
}
