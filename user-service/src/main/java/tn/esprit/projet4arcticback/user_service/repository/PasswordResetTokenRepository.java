package tn.esprit.projet4arcticback.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet4arcticback.user_service.entity.PasswordResetToken;
import tn.esprit.projet4arcticback.user_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findByToken(String token);
    List<PasswordResetToken> findByUser(User user);}
