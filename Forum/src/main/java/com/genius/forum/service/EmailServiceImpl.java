package com.genius.forum.service;/*package com.example.forum.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            // Définir l'expéditeur
            helper.setFrom("ahmed.neji@esprit.tn");  // Remplace par ton adresse email
            // Définir le destinataire
            helper.setTo(to);
            // Définir le sujet
            helper.setSubject(subject);
            // Définir le corps du message
            helper.setText(text);
            // Envoyer l'email
            javaMailSender.send(message);
        } catch (MailException | MessagingException e) {
            // Gérer les exceptions liées à l'envoi du mail
            throw new MessagingException("Erreur lors de l'envoi de l'email", e);
        }
    }
}*/
