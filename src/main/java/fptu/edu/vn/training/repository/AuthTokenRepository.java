package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.AuthToken;
import fptu.edu.vn.training.model.entity.Users;
import fptu.edu.vn.training.model.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);
    void deleteAllByUserAndType(Users user, TokenType type);
}
