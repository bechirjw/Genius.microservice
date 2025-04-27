package com.genuis.categories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection
    private Set<Long> likedBy = new HashSet<>();

    // 🔥 Logique de like
    public void like(Long userId) {
        if (!likedBy.contains(userId)) {
            likes++;
            likedBy.add(userId);
        }
    }


    public Categorie(String name, String description) {
        this.nomCategorie = name;
        this.description = description;
    }
}
