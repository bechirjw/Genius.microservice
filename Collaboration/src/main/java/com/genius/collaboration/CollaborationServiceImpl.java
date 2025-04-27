package com.genius.collaboration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.management.Notification;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CollaborationServiceImpl implements ICollaborationService {
    @Autowired
    private  CollaborationRepository collaborationRepository;
    @Autowired
    private JavaMailSender mailSender;

    public List<Collaboration> retrieveAllCollaborations() {
        return collaborationRepository.findAll();
    }

    public Collaboration retrieveCollaboration(Long collaborationId) {
        return collaborationRepository.findById(collaborationId).orElse(null);
    }

//    public Collaboration addCollaboration(Collaboration c) {
//        return collaborationRepository.save(c);
//    }
private void sendNotificationToProjectOwner(Collaboration collaboration) {
    try {
        RestTemplate restTemplate = new RestTemplate();

        // Fetch project id and owner id
        Long projetId = collaboration.getProjetId();
        Long ownerId = 1L; // 🚨 Make sure your Projet has a getOwnerId()

        // Create notification object
        NotificationRequest notification = new NotificationRequest();
        notification.setMessage("A new collaboration request on your project!");
        notification.setReceiverId(ownerId);
        notification.setType("NEW_COLLABORATION");
        notification.setProjetId(projetId);

        // Send notification
        restTemplate.postForObject("http://localhost:5220/api/notifications/send", notification, Notification.class);

    } catch (Exception e) {
        System.err.println("Failed to send notification: " + e.getMessage());
    }
}

    public Collaboration addCollaboration(Collaboration c) {
    Collaboration savedCollaboration = collaborationRepository.save(c);

    // 🔥 After saving collaboration, send notification
    sendNotificationToProjectOwner(savedCollaboration);

    return savedCollaboration;
}

    public void removeCollaboration(Long collaborationId) {
        collaborationRepository.deleteById(collaborationId);
    }

    public Collaboration modifyCollaboration(Collaboration collaboration) {
        return collaborationRepository.save(collaboration);
    }

    public List<Collaboration> findAllCollaborationsByProjet(Long projetId) {
        return collaborationRepository.findAllByProjetId(projetId);
    }

    @Override
    public Collaboration accepterCollaboration(Long id) {
        Optional<Collaboration> optional = collaborationRepository.findById(id);
        if (optional.isPresent()) {
            Collaboration collab = optional.get();
            collab.setStatut("Accepté");
            Collaboration saved = collaborationRepository.save(collab);

            // Envoi d'e-mail statique
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("tayssirsboui@gmail.com");
            message.setSubject("🤝 Collaboration acceptée - Projet: " );
            message.setText("Bonjour,\n\nVotre demande de collaboration pour le projet  a été acceptée.\n\nBienvenue dans l'équipe !\n\nCordialement,\nL'équipe Projet");

            mailSender.send(message);

            return saved;
        }
        return null;
    }

}
