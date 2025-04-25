package com.genius.projet;

import com.genius.projet.client.CollaborationClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class ProjetServiceImpl implements IProjetService {
    @Autowired
    private  ProjetRepository projetRepository;
    @Autowired
    private  CollaborationClient client;



    public List<Projet> retrieveAllProjets() {
        return projetRepository.findAll();
    }

    public Projet retrieveProjet(Long projetId) {
        return projetRepository.findById(projetId).orElse(null);
    }

    public Projet addProjet(Projet p) {
        return projetRepository.save(p);
    }

    public void removeProjet(Long projetId) {
        projetRepository.deleteById(projetId);
    }

    public Projet modifyProjet(Projet projet) {
        return projetRepository.save(projet);
    }





    public FullProjetResponse findProjetsWithCollaborations(Long projetId) {
        var projetOptional = projetRepository.findById(projetId);

        if (projetOptional.isPresent()) {
            var projet = projetOptional.get(); // ✅ le projet récupéré

            // 🔁 Appel au repository des tâches pour ce projet
            List<Tache> taches = projet.getTaches(); // ou tacheRepository.findByProjetId(projetId) si tu préfères

            return FullProjetResponse.builder()
                    .titre(projet.getTitre())
                    .description(projet.getDescription())
                    .categorie(projet.getCategorie())
                    .statut(projet.getStatut())
                    .dateCreation(projet.getDateCreation())
                    .dateFinPrevue(projet.getDateFinPrevue())
                    .nombreMaxCollaborateurs(projet.getNombreMaxCollaborateurs())
                    .competencesRequises(projet.getCompetencesRequises())
                    .taches(taches) // ✅ Ajout des tâches dans la réponse
                    .collaborations(client.findAllCollaborationsByProjet(projetId)) // ✅ Ajout des collaborations depuis Feign
                    .build();

        } else {
            throw new RuntimeException("Projet not found");
        }
    }


}
