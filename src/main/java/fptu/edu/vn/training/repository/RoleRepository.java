package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.Role;
import fptu.edu.vn.training.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserRole roleName);
    boolean existsByRoleName(String roleName);
}
