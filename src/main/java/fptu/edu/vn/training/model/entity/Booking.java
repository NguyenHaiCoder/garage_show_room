package fptu.edu.vn.training.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "booking_time", nullable = false)
    private LocalDate bookingTime;

    @Column(name = "vehicle_name", length = 255)
    private String vehicleName;

    @Column(name = "booking_status")
    private Integer bookingStatus;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(length = 255)
    private String reason;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Column(name = "issue_picture", columnDefinition = "TEXT")
    private String issuePicture;
}
