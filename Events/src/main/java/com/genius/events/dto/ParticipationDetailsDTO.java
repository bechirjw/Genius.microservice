package com.genius.events.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationDetailsDTO {
    private Long evenementId;
    private Long utilisateurId;
    private String nomUtilisateur;

    private String emailUtilisateur;
    private String statut;
}
