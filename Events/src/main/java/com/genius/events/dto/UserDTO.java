package com.genius.events.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String profilePictureUrl;
    private String prenom;

    // Constructeurs
    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, String profilePictureUrl,String prenom) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.prenom = prenom;
    }

}
