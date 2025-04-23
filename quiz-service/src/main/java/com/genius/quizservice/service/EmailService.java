package com.genius.quizservice.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final String API_KEY = "";  // Replace with your SendGrid API key

    public void sendEmail(String to, String subject, String body) {
        Email from = new Email("");
        Email toEmail = new Email(to);
        Content content = new Content("text/html", body); // You can also use "text/plain"
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
        }
    }
}
