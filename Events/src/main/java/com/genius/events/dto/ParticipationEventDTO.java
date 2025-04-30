package com.genius.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationEventDTO {
    private Long participationId;
    private String titre;
    private String lieu;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
