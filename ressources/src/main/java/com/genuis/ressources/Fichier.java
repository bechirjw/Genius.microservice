package com.genuis.ressources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fichier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFichier;  // Identifiant unique du fichier

    private String nom;  // Nom du fichier

    private String filePath;  // Chemin du fichier sur le serveur

    @ManyToOne
    @JoinColumn(name = "idRessource")
    @JsonIgnore // Relation ManyToOne avec Ressource
    private Ressource ressource;  // Ressource à laquelle ce fichier est associé
}
