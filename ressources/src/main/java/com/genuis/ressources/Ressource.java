package com.genuis.ressources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRessource;

    private String titre; // Mappé automatiquement à la colonne "titre"

    private String description; // Mappé automatiquement à la colonne "description"

    @Enumerated(EnumType.STRING)
    private TypeRessource type; // Mappé automatiquement à la colonne "type"

    private LocalDateTime dateAjout = LocalDateTime.now(); // Mappé automatiquement à la colonne "dateAjout"

    @Enumerated(EnumType.STRING)
    private StatutRessource statut;

   // @ManyToOne
   // private Categorie categorie;
}