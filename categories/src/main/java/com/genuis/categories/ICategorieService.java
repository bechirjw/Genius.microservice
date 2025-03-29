package com.genuis.categories;



import java.util.List;

public interface ICategorieService {
    List<Categorie> retrieveAllCategories();

    Categorie retrieveCategorie(Long idCategorie);

    Categorie addCategorie(Categorie categorie);

    void removeCategorie(Long idCategorie);

    Categorie modifyCategorie(Categorie categorie);

    FullResources retrieveCategoriewithresources(Long idCategorie);
}
