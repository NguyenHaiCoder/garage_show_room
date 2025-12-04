//package fptu.edu.vn.training.model.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.math.BigDecimal;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "part")
//public class Part {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "part_id", nullable = false)
//    private Integer partId;
//
//    @Column(name = "part_name", nullable = false, length = 100)
//    private String partName;
//
//    @Column(name = "part_code", nullable = false, unique = true, length = 20)
//    private String partCode;
//
//    @Column(name = "part_stock", nullable = false)
//    private Integer partStock;
//
//    @Column(name = "part_unit", nullable = false, length = 20)
//    private String partUnit;
//
//    @Column(name = "part_price", nullable = false, precision = 24, scale = 2)
//    private BigDecimal partPrice;
//
//    @Column(name = "warranty_month", nullable = false)
//    private Integer warrantyMonth;
//
//    @Column(name = "part_quantity", nullable = false)
//    private Integer partQuantity;
//
//    @Column(name = "is_deleted", nullable = false)
//    private Integer isDeleted;
//
//    // MANY-TO-ONE: N Part → 1 Category
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private PartCategory category;
//
//    // ONE-TO-MANY: 1 Part → N PriceHistory
//    @OneToMany(mappedBy = "part", fetch = FetchType.LAZY)
//    private List<PartPriceHistory> priceHistoryList;
//}
