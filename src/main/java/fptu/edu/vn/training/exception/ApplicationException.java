package fptu.edu.vn.training.exception;

/**
 * ApplicationException
 * Dùng cho các lỗi logic nghiệp vụ hoặc lỗi hệ thống không thuộc nhóm validation hay not-found.
 * - Ví dụ: lỗi gửi email, lỗi gọi API, lỗi xử lý nghiệp vụ.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException() {
        super("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
