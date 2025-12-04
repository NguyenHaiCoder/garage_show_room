package fptu.edu.vn.training.model.request;

import fptu.edu.vn.training.model.enums.CategoryStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PartCategoryRequest {

    @Size(max = 20, message = "Mã category không được vượt quá 20 ký tự")
    private String categoryCode;

    @Size(max = 100, message = "Tên category không được vượt quá 100 ký tự")
    private String categoryName;

    private String description;

    private CategoryStatus status;
}
