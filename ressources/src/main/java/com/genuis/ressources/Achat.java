package com.genuis.ressources;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId; // juste l'ID de l'utilisateur
    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ressource ressource; // la ressource achetée

    private String dateAchat;
}
