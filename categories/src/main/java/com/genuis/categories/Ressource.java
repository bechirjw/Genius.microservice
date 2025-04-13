package com.genuis.categories;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ressource {

    private Long idRessource;

    private String titre; // Mappé automatiquement à la colonne "titre"

    private String description; // Mappé automatiquement à la colonne "description"

    private Long prix; // Le prix de la ressource
    private  String text;// Le texte de la ressource
    private String lien; // Le lien de la ressource

    @Enumerated(EnumType.STRING)
    private TypeRessource type; // Mappé automatiquement à la colonne "type"

    private LocalDateTime dateAjout = LocalDateTime.now(); // Mappé automatiquement à la colonne "dateAjout"

    @Enumerated(EnumType.STRING)
    private StatutRessource statut;

    @Lob
    private byte[] image; // Stocke l'image (avec annotation @Lob)

    @OneToMany(mappedBy = "ressource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Fichier> fichiers; // Liste de fichiers associés à cette ressource

}