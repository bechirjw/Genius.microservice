package com.genius.quizservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    // ajoute ce dont tu as besoin
}
