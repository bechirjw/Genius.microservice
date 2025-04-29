package com.genuis.categories;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CategorieDTO {
    private Long idCategorie;
    private Long idUser;
    private String nomCategorie;
    private String domaine;
    private String description;
    private LocalDateTime dateCreation;
    private Integer likes;
    private String image;

    // Getters et setters
}