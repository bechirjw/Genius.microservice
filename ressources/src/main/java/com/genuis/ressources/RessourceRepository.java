package com.genuis.ressources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.genuis.ressources.Ressource;

import java.util.List;

@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    List<Ressource> findAllByIdCategorie(Long idCategorie);


}
