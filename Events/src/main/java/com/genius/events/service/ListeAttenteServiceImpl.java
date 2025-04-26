package com.genius.events.service;

import com.genius.events.entity.Evenements;
import com.genius.events.entity.ListeAttente;
import com.genius.events.repository.ListeAttenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ListeAttenteServiceImpl implements IListeAttenteService {

    private final ListeAttenteRepository listeAttenteRepository;
    private final JavaMailSender mailSender;

    @Override
    public ListeAttente inscrire(ListeAttente demande) {
        if (demande.getEvenement() == null || demande.getEmail() == null) {
            throw new IllegalArgumentException("Événement et email sont requis.");
        }

        demande.setDateInscription(LocalDateTime.now());
        return listeAttenteRepository.save(demande);
    }

    @Override
    public void notifierPremierEnAttente(Evenements evenement) {
        System.out.println("➡️ Appel de notifierPremierEnAttente pour l’événement : " + evenement.getId());

        ListeAttente premier = listeAttenteRepository
                .findFirstByEvenementAndNotificationEnvoyeeFalseOrderByDateInscriptionAsc(evenement);

        if (premier == null) {
            System.out.println("⚠️ Aucune personne en attente pour l’événement " + evenement.getId());
            return;
        }

        System.out.println("📬 Envoi du mail à : " + premier.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(premier.getEmail());
        message.setSubject("📩 Une place s’est libérée pour : " + evenement.getTitre());
        message.setText("Bonjour,\n\nUne place est disponible pour l’événement \"" + evenement.getTitre() + "\".\nRendez-vous sur la plateforme pour vous inscrire !");

        mailSender.send(message);
        System.out.println("✅ Mail envoyé");

        premier.setNotificationEnvoyee(true);
        listeAttenteRepository.save(premier);
        System.out.println("🗃️ Statut de notification mis à jour");
    }


}

