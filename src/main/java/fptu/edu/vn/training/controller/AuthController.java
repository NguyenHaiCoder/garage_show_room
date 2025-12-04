package fptu.edu.vn.training.controller;

import fptu.edu.vn.training.exception.BadRequestException;
import fptu.edu.vn.training.model.entity.Users;
import fptu.edu.vn.training.model.enums.OtpAction;
import fptu.edu.vn.training.model.request.LoginOtpRequest;
import fptu.edu.vn.training.model.request.RegisterRequest;
import fptu.edu.vn.training.model.request.VerifyOtpRequest;
import fptu.edu.vn.training.model.response.ApiResponse;
import fptu.edu.vn.training.model.response.AuthResponse;
import fptu.edu.vn.training.repository.UserAccountRepository;
import fptu.edu.vn.training.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserAccountRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody @Valid RegisterRequest request,
            HttpServletRequest http) {
        AuthResponse data = authService.register(request, http);
        return ResponseEntity.ok(ApiResponse.success("Gửi OTP đăng ký thành công", data));
    }

    @PostMapping("/verify-register-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyRegisterOtp(
            @RequestBody @Valid VerifyOtpRequest request) {
        AuthResponse data = authService.verifyRegisterOtp(request);
        return ResponseEntity.ok(ApiResponse.success("Xác thực OTP thành công", data));
    }

    @PostMapping("/login/request-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> requestLoginOtp(
            @RequestBody @Valid LoginOtpRequest request,
            HttpServletRequest http) {
        AuthResponse data = authService.requestLoginOtp(request, http);
        return ResponseEntity.ok(ApiResponse.success("Gửi OTP đăng nhập thành công", data));
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyLoginOtp(
            @RequestBody @Valid VerifyOtpRequest request) {
        AuthResponse data = authService.verifyLoginOtp(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", data));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestParam String refreshToken) {
        AuthResponse data = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Làm mới token thành công", data));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Thiếu hoặc sai định dạng Authorization header.");
        }

        String token = authHeader.substring(7);
        authService.logout(token);

        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công", null));
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Gửi lại OTP", description = "Gửi lại mã OTP cho người dùng theo loại REGISTER hoặc LOGIN.")
    public ResponseEntity<ApiResponse<Void>> resendOtp(
            @Parameter(description = "Địa chỉ email cần gửi lại OTP", example = "example@gmail.com")
            @RequestParam String email,

            @Parameter(description = "Loại OTP cần gửi lại (REGISTER hoặc LOGIN)")
            @RequestParam OtpAction type,

            HttpServletRequest http) {

        String normalizedEmail = email.trim().toLowerCase();

        Users user = userRepo.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy người dùng với email này."));

        authService.resendOtp(user, type, http);

        String msg = (type == OtpAction.LOGIN)
                ? "OTP đăng nhập mới đã được gửi đến email " + email
                : "OTP đăng ký mới đã được gửi đến email " + email;

        return ResponseEntity.ok(ApiResponse.success(msg, null));
    }



}
