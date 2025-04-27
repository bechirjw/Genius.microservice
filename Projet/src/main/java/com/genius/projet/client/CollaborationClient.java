package com.genius.projet.client;


import com.genius.projet.Collaboration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "collaborationClient", url = "http://localhost:5210/collaboration")
public interface  CollaborationClient {
    @GetMapping("/projet/{projet-id}")
    List<Collaboration> findAllCollaborationsByProjet(@PathVariable("projet-id") Long projetId);

}
