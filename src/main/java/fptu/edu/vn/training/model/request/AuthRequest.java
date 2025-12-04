package fptu.edu.vn.training.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;
}
