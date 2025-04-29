package com.genuis.ressources;


import com.genuis.ressources.FichierDTO;
import com.genuis.ressources.StatutRessource;
import com.genuis.ressources.StatutRessourceConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RessourceResponseDTO {
    private long idCategorie;
    private Long idUser;
    private Long id;
    private String titre;
    private String description;
    @Convert(converter = StatutRessourceConverter.class)

    @Enumerated(EnumType.STRING)
    private StatutRessource statut;
    private Long prix;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String text;
    private String lien;
    private String type;
    private LocalDateTime dateAjout;
    private String imageBase64; // Encodée en base64
    private List<FichierDTO> fichiers;
}
