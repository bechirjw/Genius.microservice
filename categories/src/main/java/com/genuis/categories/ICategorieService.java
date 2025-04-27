package com.genuis.categories;



import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICategorieService {
    List<Categorie> retrieveAllCategories();

    Categorie retrieveCategorie(Long idCategorie);

    Categorie addCategorie(Categorie categorie);

    void removeCategorie(Long idCategorie);

    Categorie modifyCategorie(Categorie categorie);

    FullResources retrieveCategoriewithresources(Long idCategorie);

    Categorie addCategorieWithImage(String nomCategorie, MultipartFile image) throws IOException;
    CategorieDTO retrieveCategorieWithImage(Long idCategorie);

    Categorie updateLikes(Long idCategorie, Integer likes);
     void likeCategorie(Long categorieId, Long userId);

}
