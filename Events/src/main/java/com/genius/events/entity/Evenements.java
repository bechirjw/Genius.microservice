package com.genius.events.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evenements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String titre;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime   dateDebut;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime  dateFin;
    private String lieu;
    private String categorie;
    private Integer nbMaxParticipants;
    @Enumerated(EnumType.STRING)
    private StatutEvenement statut;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateCreation = LocalDateTime.now();
    private String image; // Il peut être null ou vide
    @Column(name = "utilisateur_id")
    private Long utilisateurId;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evenement", fetch = FetchType.EAGER, orphanRemoval = true)
   // @JsonIgnore
    @JsonIgnoreProperties("evenement") // évite la récursion
    private List<Participations> participations;

}
