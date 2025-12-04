package fptu.edu.vn.training.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
    String buildOtpEmailTemplate(String otp);
}
