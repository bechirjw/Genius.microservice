package com.genuis.categories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
    public class Categorie {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idCategorie;


        private Integer likes =0;


        private String nomCategorie; // Nom de la catégorieg

        private String domaine; // Domaine associé (ex: Informatique, Science...)

        private String description; // Description détaillée de la catégorie
        private LocalDateTime dateCreation  = LocalDateTime.now();
    @Lob
    private byte[] image;

}
