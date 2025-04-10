package com.genius.projet;

import jakarta.persistence.ElementCollection;
import lombok.*;
import lombok.Builder;


import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullProjetResponse {

    private String titre;
    private String description;
    private String categorie;
    private String statut;
    private Date dateCreation;
    private Date dateFinPrevue;
    private int nombreMaxCollaborateurs;

    @ElementCollection
    private List<String> competencesRequises;


     List<Collaboration> collaborations;

}
