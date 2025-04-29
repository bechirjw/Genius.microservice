package tn.esprit.projet4arcticback.user_service.service;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.projet4arcticback.user_service.RestController.AuthentificationRequest;
import tn.esprit.projet4arcticback.user_service.RestController.AuthentificationResponse;
import tn.esprit.projet4arcticback.user_service.RestController.RegistrationRequest;
import tn.esprit.projet4arcticback.user_service.email.EmailServer;
import tn.esprit.projet4arcticback.user_service.email.EmailTemplateName;
import tn.esprit.projet4arcticback.user_service.entity.Token;
import tn.esprit.projet4arcticback.user_service.entity.User;
import tn.esprit.projet4arcticback.user_service.repository.TokenRepository;
import tn.esprit.projet4arcticback.user_service.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthentificationService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailServer emailServer;
    private final JwtService jwtService;
    private final AuthenticationManager authentificationManager;
    @Value("${application.security.mailing.frontend.activation-url}")
    private String activationUrl;


    public AuthentificationResponse register(RegistrationRequest request) throws MessagingException {
        var user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .enabled(false)
                .image(request.getImage())
                .accountLocked(false)
                .roles(request.getRoles())
                .build();

        userRepository.save(user);
       // sendValidationEmail(user);

        var claims = new HashMap<String, Object>();
        claims.put("id", user.getIdUser());
        claims.put("fullName", user.fullName());
        claims.put("role", user.getRoles().name());


        String jwtToken = jwtService.generateToken2(claims, user);

        return AuthentificationResponse.builder()
                .token(jwtToken)
                .id(user.getIdUser()) // Include ID
                .email(user.getEmail()) // Include email
                .build();
    }


    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailServer.sendMail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "account Activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(20))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;

    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public AuthentificationResponse authenticate(AuthentificationRequest request) {
        var auth = authentificationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("id", user.getIdUser());
        claims.put("fullName", user.fullName());
        claims.put("role", user.getRoles().name());  // On récupère le nom du rôle (ADMIN, ENTREPRENEUR, etc.)
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 24);
        var jwtToken = jwtService.generateToken2(claims , user);
        return AuthentificationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findTokenByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiredAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired, A new token has been send to the same email ");
        }

        var user = userRepository.findById(savedToken.getUser().getIdUser()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.save(savedToken);

    }
}