package com.genius.events.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Enumerated(EnumType.STRING )
    private StatutParticipation statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "evenement_id")

    private Evenements  evenement;
    @Column(name = "utilisateur_id")
    private Long utilisateurId; // temporaire, jusqu’à ce que l'entité Utilisateur soit intégrée

    @Column(name = "FullName")
    private String nomUtilisateur;



    @Column(name = "email_utilisateur")
    private String emailUtilisateur;

}
