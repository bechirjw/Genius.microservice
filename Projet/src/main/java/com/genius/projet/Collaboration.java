package com.genius.projet;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collaboration {


    private String role;
    private String statut;
    private Date dateDemande;
    private Date dateValidation;
    private Long projetId;
}
