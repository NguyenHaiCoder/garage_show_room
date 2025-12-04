package fptu.edu.vn.training.model.response;

import fptu.edu.vn.training.model.enums.CategoryStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartCategoryResponse {
    private Integer categoryId;
    private String categoryCode;
    private String categoryName;
    private String description;
    private CategoryStatus status;
}
