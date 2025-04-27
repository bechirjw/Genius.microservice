package com.genuis.categories;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriService {

    private  FavoriRepository favoriRepository;
    private  CategorieRepository categorieRepository;

    public FavoriService(FavoriRepository favoriRepository, CategorieRepository categorieRepository) {
        this.favoriRepository = favoriRepository;
        this.categorieRepository = categorieRepository;
    }

    public void ajouterFavori(Long userId, Long categorieId) {
        // Vérifier que la catégorie existe
        categorieRepository.findById(categorieId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        // Vérifier si le favori existe déjà
        favoriRepository.findByUserIdAndCategorieId(userId, categorieId)
                .ifPresent(f -> {
                    throw new RuntimeException("Déjà en favori");
                });

        Favori favori = new Favori();
        favori.setUserId(userId);
        favori.setCategorieId(categorieId);
        favoriRepository.save(favori);
    }

    public void retirerFavori(Long userId, Long categorieId) {
        Favori favori = favoriRepository.findByUserIdAndCategorieId(userId, categorieId)
                .orElseThrow(() -> new RuntimeException("Favori non trouvé"));

        favoriRepository.delete(favori);
    }

    public List<Categorie> getFavorisByUser(Long userId) {
        List<Favori> favoris = favoriRepository.findByUserId(userId);
        List<Long> categorieIds = favoris.stream()
                .map(Favori::getCategorieId)
                .toList();

        return categorieRepository.findAllById(categorieIds);
    }
}

