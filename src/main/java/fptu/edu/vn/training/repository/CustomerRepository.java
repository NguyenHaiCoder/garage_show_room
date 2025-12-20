package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findFirstByCustomerEmailAndIsDeleted(String email, Integer isDeleted);
}
