package com.genuis.categories;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ressource {

    private Long idRessource;

    private String titre; // Mappé automatiquement à la colonne "titre"

    private String description; // Mappé automatiquement à la colonne "description"

    @Enumerated(EnumType.STRING)
    private TypeRessource type; // Mappé automatiquement à la colonne "type"

    private LocalDateTime dateAjout = LocalDateTime.now(); // Mappé automatiquement à la colonne "dateAjout"

    @Enumerated(EnumType.STRING)
    private StatutRessource statut;


}