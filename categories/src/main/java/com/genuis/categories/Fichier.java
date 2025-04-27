package com.genuis.categories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fichier {


    private Long idFichier;  // Identifiant unique du fichier

    private String nom;  // Nom du fichier

    private String filePath;  // Chemin du fichier sur le serveur

    @ManyToOne
    @JoinColumn(name = "idRessource")
    @JsonIgnore // Relation ManyToOne avec Ressource
    private Ressource ressource;  // Ressource à laquelle ce fichier est associé
}
