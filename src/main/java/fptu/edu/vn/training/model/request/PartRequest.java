package fptu.edu.vn.training.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PartRequest {

    @NotBlank(message = "Tên phụ tùng không được để trống")
    private String partName;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private Double partPrice;

    @NotBlank(message = "Đơn vị tính không được để trống")
    private String partUnit;

    @NotNull(message = "Số tháng bảo hành không được để trống")
    @Min(value = 0, message = "Bảo hành phải >= 0")
    private Integer numberMonthWarranty;

    @NotNull(message = "Số lượng trên kệ không được để trống")
    @Min(value = 0, message = "Số lượng kệ phải >= 0")
    private Long shelfStock;

    @NotNull(message = "Số lượng trong kho không được để trống")
    @Min(value = 0, message = "Số lượng kho phải >= 0")
    private Long warehouseStock;

    @NotNull(message = "Mức cảnh báo tồn kho không được để trống")
    private Integer minStockLevel;

    @NotBlank(message = "Tên nhóm phụ tùng không được để trống")
    private String categoryName;

    private String description;
}
