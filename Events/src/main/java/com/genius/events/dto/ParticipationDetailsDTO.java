package com.genius.events.dto;

import com.genius.events.entity.StatutParticipation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationDetailsDTO {
    private Long evenementId;
    private Long utilisateurId;
    private StatutParticipation statut;

    private String nomUtilisateur;
    private String emailUtilisateur;
}
