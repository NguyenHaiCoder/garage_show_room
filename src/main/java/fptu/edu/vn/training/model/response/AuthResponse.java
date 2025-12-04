package fptu.edu.vn.training.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String message;
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;
    private LocalDateTime otpExpireAt;
    private String accessToken;
    private String refreshToken;
}
