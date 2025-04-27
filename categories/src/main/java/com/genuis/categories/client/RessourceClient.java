package com.genuis.categories.client;

import com.genuis.categories.Ressource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ressourceClient", url = "http://localhost:5010/ressources")
public interface RessourceClient {

    @GetMapping("/categorie/{categorie-id}")
    List<Ressource> retrieveRessourceByCategorie(@PathVariable("categorie-id") Long idCategorie);
}