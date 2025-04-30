package com.genius.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPostDeletionEmail(String title, String firstName, String email) {
        String subject = "Post Deleted Notification";
        String body = "Hello " + firstName + ",\n\n" +
                "Your post titled '" + title + "' has been deleted by our admin team for not complying with our community guidelines.\n" +
                "If you believe this was a mistake, please contact us.\n\n" +
                "Thank you for your understanding.\n\n" +
                "Aymen Thabet, FureverBuddy Admin Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}