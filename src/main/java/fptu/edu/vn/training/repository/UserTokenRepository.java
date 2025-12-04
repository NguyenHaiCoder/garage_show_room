package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.UserToken;
import fptu.edu.vn.training.model.enums.TokenStatus;
import fptu.edu.vn.training.model.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    List<UserToken> findAllByUser_UserIdAndStatus(Integer userId, TokenStatus status);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM UserToken t WHERE (t.accessToken = :token OR t.refreshToken = :token) AND t.status = 'ACTIVE'")
    boolean isTokenStillActive(String token);

    @Query("SELECT t FROM UserToken t WHERE " +
            "(t.accessToken = :token OR t.refreshToken = :token) " +
            "AND t.type = :type AND t.status = :status")
    Optional<UserToken> findByTokenValueAndTypeAndStatus(String token, TokenType type, TokenStatus status);
}
