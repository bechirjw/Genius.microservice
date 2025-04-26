package com.genuis.ressources;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchatService {

    private final AchatRepository achatRepository;
    private final RessourceRepository ressourceRepository;

    public Achat enregistrerAchat(Long utilisateurId, Long ressourceId) {
        Ressource ressource = ressourceRepository.findById(ressourceId)
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));

        Achat achat = Achat.builder()
                .utilisateurId(utilisateurId)
                .ressource(ressource)
                .dateAchat(LocalDate.now().toString())
                .build();

        return achatRepository.save(achat);
    }

    public List<Ressource> getRessourcesAcheteesParUtilisateur(Long utilisateurId) {
        List<Achat> achats = achatRepository.findByUtilisateurId(utilisateurId);
        return achats.stream()
                .map(Achat::getRessource)
                .toList();
    }

}
