package fptu.edu.vn.training.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email không hợp lệ")
    @NotBlank
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
