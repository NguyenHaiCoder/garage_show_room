package fptu.edu.vn.training.exception;

import java.util.Map;

/**
 * ValidationException
 * Dùng để mô tả lỗi khi dữ liệu client gửi lên không hợp lệ.
 * - Ví dụ: thiếu trường bắt buộc, format email sai, giá trị không hợp lệ.
 */
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    /**
     * Tạo exception chỉ với message chung (dùng khi không có map lỗi chi tiết)
     */
    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }

    /**
     * Tạo exception có map chi tiết lỗi từng field
     */
    public ValidationException(Map<String, String> errors) {
        super("Dữ liệu gửi lên không hợp lệ");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
