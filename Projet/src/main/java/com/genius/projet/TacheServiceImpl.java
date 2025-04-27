package com.genius.projet;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class TacheServiceImpl implements ITacheService{


    @Autowired
    private  TacheRepository tacheRepository;
    @Autowired
    private  ProjetRepository projetRepository;

    // 1. Récupérer toutes les tâches
    public List<Tache> retrieveAllTaches() {
        return tacheRepository.findAll();
    }

    // 2. Récupérer une tâche par ID
    public Tache retrieveTache(Long id) {
        return tacheRepository.findById(id).orElse(null);
    }

    // 3. Ajouter une tâche liée à un projet
    public Tache addTache(Tache t, Long projetId) {
        Projet projet = projetRepository.findById(projetId).orElse(null);
        if (projet != null) {
            t.setProjet(projet);
            // tu peux forcer le statut initial si besoin :
            if (t.getStatut() == null) {
                t.setStatut("To Do");
            }
            return tacheRepository.save(t);
        }
        return null;
    }

    // 4. Supprimer une tâche
    public void removeTache(Long id) {
        tacheRepository.deleteById(id);
    }

    // 5. Modifier une tâche
    @Override
    public Tache modifyTache(Tache t) {
        Tache existing = tacheRepository.findById(t.getId()).orElseThrow();

        existing.setTitre(t.getTitre());
        existing.setDescription(t.getDescription());
        existing.setEstimation(t.getEstimation());
        existing.setPriorite(t.getPriorite());
        existing.setStatut(t.getStatut());

        if (t.getProjet() != null && t.getProjet().getId() != null) {
            Projet projet = projetRepository.findById(t.getProjet().getId()).orElseThrow();
            existing.setProjet(projet);
        }

        return tacheRepository.save(existing);
    }

    // 6. Récupérer toutes les tâches d’un projet
    public List<Tache> getTachesByProjet(Long projetId) {
        return tacheRepository.findByProjetId(projetId);
    }

    @Override
    public Tache updateStatut(Long tacheId, String newStatut) {
        Tache tache = tacheRepository.findById(tacheId).orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        tache.setStatut(newStatut);
        return tacheRepository.save(tache);
    }

}
