package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.OtpVerification;
import fptu.edu.vn.training.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, String> {

    @Query("SELECT o FROM OtpVerification o WHERE o.email = :email ORDER BY o.createdAt DESC")
    List<OtpVerification> findRecentByEmail(String email);

    long countByEmailAndCreatedAtAfter(String email, LocalDateTime threshold);

    @Modifying
    @Query("UPDATE OtpVerification o SET o.status = 'EXPIRED' " +
            "WHERE o.expireAt < :now AND o.status <> 'VERIFIED'")
    int updateExpiredOtps(@Param("now") LocalDateTime now);

    @Query("SELECT o FROM OtpVerification o " +
            "WHERE o.user = :user AND o.email = :email " +
            "ORDER BY o.createdAt DESC LIMIT 1")
    Optional<OtpVerification> findLatestByUserAndEmail(@Param("user") Users user,
                                                       @Param("email") String email);



}
