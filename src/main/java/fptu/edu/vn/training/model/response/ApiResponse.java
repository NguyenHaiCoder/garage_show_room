package fptu.edu.vn.training.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import fptu.edu.vn.training.model.enums.StatusEnumAPI;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 🚀 ApiResponse<T>
 * Chuẩn hóa format phản hồi cho toàn bộ GaragePro API.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private StatusEnumAPI status;
    private String message;
    private T data;
    private ErrorDetail error;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.SUCCESS)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.SUCCESS)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data, Map<String, Object> metadata) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.SUCCESS)
                .message(message)
                .data(data)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> warning(String message, Map<String, Object> metadata) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.WARNING)
                .message(message)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.ERROR)
                .message(message)
                .error(ErrorDetail.builder()
                        .message(message)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.ERROR)
                .message(message)
                .error(ErrorDetail.builder()
                        .code(code)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, Map<String, String> fieldErrors) {
        return ApiResponse.<T>builder()
                .status(StatusEnumAPI.ERROR)
                .message(message)
                .error(ErrorDetail.builder()
                        .code(code)
                        .fields(fieldErrors)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        private String code;
        private String message;
        private Map<String, String> fields;
    }
}