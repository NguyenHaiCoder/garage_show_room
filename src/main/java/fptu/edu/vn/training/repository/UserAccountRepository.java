package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
}
