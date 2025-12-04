package fptu.edu.vn.training.handler;

import fptu.edu.vn.training.exception.*;
import fptu.edu.vn.training.model.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Validation errors (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError f : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(f.getField(), f.getDefaultMessage());
        }
        log.warn("[Validation] {}", fieldErrors);
        return ApiResponse.error("VALIDATION_ERROR", "Dữ liệu đầu vào không hợp lệ", fieldErrors);
    }

    // ✅ Constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraint(ConstraintViolationException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(v -> {
            String field = v.getPropertyPath().toString();
            fieldErrors.put(field, v.getMessage());
        });
        return ApiResponse.error("VALIDATION_ERROR", "Tham số không hợp lệ", fieldErrors);
    }

    // ✅ BadRequest
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBadRequest(BadRequestException ex) {
        log.warn("[BadRequest] {}", ex.getMessage());
        return ApiResponse.error("BAD_REQUEST", ex.getMessage());
    }

    // ✅ InvalidUserStatus (403)
    @ExceptionHandler(InvalidUserStatusException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleInvalidStatus(InvalidUserStatusException ex) {
        log.warn("[InvalidUserStatus] {}", ex.getMessage());
        return ApiResponse.error("INVALID_USER_STATUS", ex.getMessage());
    }

    // ✅ Unauthorized (401)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleUnauthorized(UnauthorizedException ex) {
        log.warn("[Unauthorized] {}", ex.getMessage());
        return ApiResponse.error("UNAUTHORIZED", ex.getMessage());
    }

    // ✅ AccessDenied & InvalidToken (403)
    @ExceptionHandler({AccessDeniedException.class, InvalidTokenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleForbidden(Exception ex) {
        String msg = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "Token không hợp lệ hoặc quyền truy cập bị từ chối";
        log.warn("[Forbidden] {}", msg);
        return ApiResponse.error("FORBIDDEN", msg);
    }

    // ✅ NotFound (404)
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(NotFoundException ex) {
        log.warn("[NotFound] {}", ex.getMessage());
        return ApiResponse.error("NOT_FOUND", ex.getMessage());
    }

    // ✅ IllegalArgument
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("[IllegalArgument] {}", ex.getMessage());
        return ApiResponse.error("ILLEGAL_ARGUMENT", ex.getMessage());
    }

    // ✅ Security
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleSecurity(SecurityException ex) {
        log.warn("[Security] {}", ex.getMessage());
        return ApiResponse.error("SECURITY_ERROR", ex.getMessage());
    }

    // ✅ NoHandler (404)
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNoHandler(org.springframework.web.servlet.NoHandlerFoundException ex) {
        String msg = "Không tìm thấy endpoint " + ex.getHttpMethod() + " " + ex.getRequestURL();
        log.warn("[NoHandler] {}", msg);
        return ApiResponse.error("ENDPOINT_NOT_FOUND", msg);
    }

    // ✅ Runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleRuntime(RuntimeException ex) {
        log.error("[RuntimeException] {}", ex.getMessage(), ex);
        return ApiResponse.error("INTERNAL_ERROR", ex.getMessage());
    }

    // ✅ Catch-all
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleAny(Exception ex) {
        String msg = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : "Đã xảy ra lỗi trong hệ thống, vui lòng thử lại sau";
        log.error("[UnhandledException] {}", msg, ex);
        return ApiResponse.error("INTERNAL_ERROR", msg);
    }
}