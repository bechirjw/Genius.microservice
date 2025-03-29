package com.genuis.categories;

import com.genuis.categories.client.RessourceClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CategorieSercieImpl implements ICategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private RessourceClient ressourceClient;

    @Override
    public List<Categorie> retrieveAllCategories() {
        return categorieRepository.findAll();
    }

    @Override
    public Categorie retrieveCategorie(Long idCategorie) {
        return categorieRepository.findById(idCategorie).orElse(null);
    }

    @Override
    public FullResources retrieveCategoriewithresources(Long idCategorie) {
        var categorieOptional = categorieRepository.findById(idCategorie);

        if (categorieOptional.isPresent()) {
            var categorie = categorieOptional.get();
            return FullResources.builder()
                    .idCategorie(categorie.getIdCategorie())
                    .nomCategorie(categorie.getNomCategorie())
                    .ressources(ressourceClient.retrieveRessourceByCategorie(idCategorie))
                    .build();
        } else {
            throw new RuntimeException("Categorie not found");
        }
    }

    @Override
    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    @Override
    public void removeCategorie(Long idCategorie) {
        categorieRepository.deleteById(idCategorie);
    }

    @Override
    public Categorie modifyCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }
}