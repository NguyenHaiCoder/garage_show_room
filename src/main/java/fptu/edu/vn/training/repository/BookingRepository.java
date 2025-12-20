package fptu.edu.vn.training.repository;

import fptu.edu.vn.training.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = "SELECT c.customer_id AS customerId, c.customer_name AS fullName, " +
            "c.customer_email AS email, c.customer_phone AS phone " +
            "FROM bookings b " +
            "JOIN customer c ON b.customer_id = c.customer_id " +
            "WHERE c.customer_email = :email " +
            "AND (b.is_deleted IS NULL OR b.is_deleted = 0) " +
            "AND (c.is_deleted IS NULL OR c.is_deleted = 0) " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<BookingCustomerInfo> findCustomerInfoByEmail(@Param("email") String email);
}
