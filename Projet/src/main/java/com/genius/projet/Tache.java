package com.genius.projet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String statut; // par défaut "To Do"
    private String priorite; // High, Medium, Low
    private String estimation; // exemple "2 jours"

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @JsonBackReference

    private Projet projet;

}
