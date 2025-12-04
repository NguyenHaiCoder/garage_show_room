//package fptu.edu.vn.training.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "part_price_history")
//public class PartPriceHistory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "history_id")
//    private Integer historyId;
//
//    @Column(name = "history_price", nullable = false, precision = 24, scale = 2)
//    private BigDecimal historyPrice;
//
//    @Column(name = "date_history", nullable = false)
//    private LocalDateTime dateHistory;
//
//    @Column(name = "modified_by", nullable = false)
//    private Integer modifiedBy;
//
//    @Column(name = "is_deleted", nullable = false)
//    private Integer isDeleted;
//
//    // MANY-TO-ONE: nhiều history → 1 part
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "part_id", nullable = false)
//    private Part part;
//}
