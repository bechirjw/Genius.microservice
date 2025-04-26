package com.genuis.categories;

import jakarta.persistence.*;

@Entity
@Table(name = "favoris")
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;       // l'utilisateur qui a mis en favori
    private Long categorieId;  // la catégorie mise en favori

    // ==== Constructeurs ====

    public Favori() {
        // Constructeur vide (obligatoire pour JPA)
    }

    public Favori(Long userId, Long categorieId) {
        this.userId = userId;
        this.categorieId = categorieId;
    }

    // ==== Getters et Setters ====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }
}
