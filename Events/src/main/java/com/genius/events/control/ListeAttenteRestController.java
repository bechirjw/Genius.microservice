package com.genius.events.control;

import com.genius.events.entity.ListeAttente;
import com.genius.events.service.IListeAttenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/liste-attente")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ListeAttenteRestController {

    private final IListeAttenteService listeAttenteService;

    @PostMapping("/inscrire") // ✅ le chemin attendu est /liste-attente/inscrire
    public ResponseEntity<ListeAttente> inscrire(@RequestBody ListeAttente demande) {
        ListeAttente saved = listeAttenteService.inscrire(demande);
        return ResponseEntity.ok(saved);
    }
}




