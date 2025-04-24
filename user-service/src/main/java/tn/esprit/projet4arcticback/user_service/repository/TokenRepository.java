package tn.esprit.projet4arcticback.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet4arcticback.user_service.entity.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token , Long> {
    Optional<Token> findTokenByToken(String token);
}
