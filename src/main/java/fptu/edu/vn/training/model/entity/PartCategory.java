//package fptu.edu.vn.training.model.entity;
//
//import fptu.edu.vn.training.model.enums.CategoryStatus;
//import jakarta.persistence.*;
//import lombok.*;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "part_category")
//public class PartCategory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "category_id")
//    private Integer categoryId;
//
//    @Column(name = "category_code", nullable = false, unique = true, length = 20)
//    private String categoryCode;
//
//    @Column(name = "category_name", nullable = false, length = 100)
//    private String categoryName;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "part_stock", nullable = false)
//    private Integer partStock;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private CategoryStatus status;
//
//    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
//    private List<Part> parts;
//}
