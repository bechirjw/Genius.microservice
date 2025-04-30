package com.genius.projet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String categorie;
    private String statut;
    private Date dateCreation;
    private Date dateFinPrevue;
    private int nombreMaxCollaborateurs;

    private Long userId; // 🆕 Ajout ici (id de l'entrepreneur créateur)

    @ElementCollection
    private List<String> competencesRequises;


    @OneToMany(mappedBy = "projet", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Tache> taches = new ArrayList<>();


}
