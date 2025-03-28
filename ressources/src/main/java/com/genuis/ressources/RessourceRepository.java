package com.genuis.ressources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.genuis.ressources.Ressource;
@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {
}
