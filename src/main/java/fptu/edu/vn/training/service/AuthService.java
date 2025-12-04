package fptu.edu.vn.training.service;

import fptu.edu.vn.training.model.entity.Users;
import fptu.edu.vn.training.model.enums.OtpAction;
import fptu.edu.vn.training.model.request.*;
import fptu.edu.vn.training.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request, HttpServletRequest http);
    AuthResponse verifyRegisterOtp(VerifyOtpRequest request);
    AuthResponse requestLoginOtp(LoginOtpRequest request, HttpServletRequest http);
    AuthResponse verifyLoginOtp(VerifyOtpRequest request);
    AuthResponse refreshToken(String refreshToken);
    void resendOtp(Users user, OtpAction action, HttpServletRequest http);
    void logout(String token);
}
