package tn.esprit.projet4arcticback.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.projet4arcticback.user_service.email.EmailServer;
import tn.esprit.projet4arcticback.user_service.email.EmailTemplateName;
import tn.esprit.projet4arcticback.user_service.entity.PasswordResetToken;
import tn.esprit.projet4arcticback.user_service.entity.User;
import tn.esprit.projet4arcticback.user_service.repository.PasswordResetTokenRepository;
import tn.esprit.projet4arcticback.user_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailServer emailServer;
    private final PasswordEncoder passwordEncoder;

    public void requestPasswordReset(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Invalider les tokens existants
        tokenRepository.findByUser(user)
                .forEach(token -> {
                    token.setUsed(true);
                    tokenRepository.save(token);
                });

        // Créer un nouveau token
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        token.setUsed(false);
        tokenRepository.save(token);

        // Envoyer l'email
        String resetUrl = "http://localhost:4200/#/reset-password?token=" + token.getToken();
        try {
            emailServer.sendMail(
                    user.getEmail(),
                    user.fullName(),
                    EmailTemplateName.RESET_PASSWORD,
                    resetUrl,
                    null,
                    "Réinitialisation de votre mot de passe"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token déjà utilisé");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }

        User user = resetToken.getUser();
        user.setMotDePasse(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}