package com.genuis.ressources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ressource {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRessource;

    private String titre; // Le titre de la ressource

    private String description; // La description de la ressource

    private Long prix; // Le prix de la ressource
    private  String text;// Le texte de la ressource
    private String lien; // Le lien de la ressource
    @Enumerated(EnumType.STRING)
    private TypeRessource type; // Le type de la ressource

    private LocalDateTime dateAjout = LocalDateTime.now(); // La date d'ajout de la ressource
    @Convert(converter = StatutRessourceConverter.class)
    @Enumerated(EnumType.STRING)
    private StatutRessource statut; // Statut de la ressource

    @Lob
    private byte[] image; // Stocke l'image (avec annotation @Lob)

    @OneToMany(mappedBy = "ressource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Fichier> fichiers; // Liste de fichiers associés à cette ressource

    // Récupérer l'idCategorie depuis le path
    private Long idCategorie; // ID de la catégorie à laquelle appartient cette ressource
}
