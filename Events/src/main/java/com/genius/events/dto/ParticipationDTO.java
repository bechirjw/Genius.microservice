package com.genius.events.dto;

import com.genius.events.entity.StatutParticipation;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ParticipationDTO {
    private Long evenementId;
    private StatutParticipation statut;
    private Long utilisateurId; // pour calendar
}