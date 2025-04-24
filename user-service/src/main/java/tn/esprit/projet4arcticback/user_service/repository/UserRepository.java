package tn.esprit.projet4arcticback.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet4arcticback.user_service.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    boolean existsByEmail(String email);


}
