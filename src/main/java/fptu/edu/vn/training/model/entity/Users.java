package fptu.edu.vn.training.model.entity;

import fptu.edu.vn.training.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String fullName;

    private String email;
    private String phone;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(mappedBy = "user")
    private List<AuditLog> auditLogs;
}
