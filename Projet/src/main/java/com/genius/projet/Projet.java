package com.genius.projet;

import jakarta.persistence.*;
import lombok.*;

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

    @ElementCollection
    private List<String> competencesRequises;


}
