package com.genius.events.control;

import com.genius.events.entity.Evenements;
import com.genius.events.repository.EvenementsRepository;
import com.genius.events.service.IListeAttenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestMailController {

    private final IListeAttenteService listeAttenteService;
    private final EvenementsRepository evenementsRepository;

    @GetMapping("/notifier/{id}")
    public void testNotif(@PathVariable Long id) {
        Evenements event = evenementsRepository.findById(id).orElseThrow();
        listeAttenteService.notifierPremierEnAttente(event);
    }
}