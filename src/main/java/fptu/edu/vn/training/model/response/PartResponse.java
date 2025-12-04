package fptu.edu.vn.training.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartResponse {

    private Integer partId;
    private String partName;
    private String partCode;

    private Double partPrice;
    private String partUnit;
    private Integer numberMonthWarranty;

    private Long shelfStock;
    private Long warehouseStock;
    private Integer minStockLevel;

    private String categoryName;

    private String description;
    private String createdByName;
}
