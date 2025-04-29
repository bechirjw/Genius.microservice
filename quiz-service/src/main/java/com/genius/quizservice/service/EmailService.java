package com.genius.quizservice.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final String API_KEY = "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";

    public void sendEmail(String to, String subject, String body) {
        Email from = new Email("oussamachatgpt01@gmail.com");
        Email toEmail = new Email(to);
        Content content = new Content("text/html", body);
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

    // ➡️ NOUVELLE METHODE professionnelle
    public void sendQuizResultEmail(String to, String fullName, String quizName, double percentage) {
        String subject = "🎯 Your Quiz Results!";

        String body = String.format("""
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="background-color: #ffffff; padding: 30px; border-radius: 8px; max-width: 600px; margin: auto;">
                  <h1 style="color: #4CAF50; text-align: center;">🎉 Congratulations!</h1>
                  <p style="text-align: center; font-size: 18px;">Hi <strong>%s</strong>,</p>
                  <p style="text-align: center; font-size: 18px;">You have successfully completed the quiz: <strong>%s</strong>.</p>
                  <p style="text-align: center; font-size: 18px;">Your Score: <strong>%.2f%%</strong></p>
                  <div style="text-align: center; margin-top: 30px;">
                    <a href="https://your-platform-link.com" style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px;">View More Quizzes</a>
                  </div>
                  <p style="text-align: center; font-size: 12px; color: #999; margin-top: 20px;">&copy; 2025 Genius Quiz Platform. All rights reserved.</p>
                </div>
              </body>
            </html>
            """, fullName, quizName, percentage);

        sendEmail(to, subject, body);
    }
}
