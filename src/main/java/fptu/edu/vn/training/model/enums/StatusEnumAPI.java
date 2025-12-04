package fptu.edu.vn.training.model.enums;

import lombok.Getter;

@Getter
public enum StatusEnumAPI {
    SUCCESS("200", "Thành công"),
    ERROR("500", "Lỗi hệ thống hoặc nghiệp vụ"),
    WARNING("300", "Cảnh báo hoặc yêu cầu xác nhận thêm");

    private final String code;
    private final String description;

    StatusEnumAPI(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
