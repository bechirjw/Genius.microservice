package com.genuis.categories;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullResources {
    private Long idCategorie;

    private String nomCategorie; // Nom de la catégorieg

    private String domaine; // Domaine associé (ex: Informatique, Science...)

    private String description; // Description détaillée de la catégorie
    private LocalDateTime dateCreation  = LocalDateTime.now();

    List<Ressource> ressources;
}
