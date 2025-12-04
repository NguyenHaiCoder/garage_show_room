package fptu.edu.vn.training.service.impl;

import fptu.edu.vn.training.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    private static final String PRIMARY_COLOR = "#FF7A00";
    private static final String DARK_BG = "#121212";
    private static final String CARD_BG = "#1E1E2E";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("GaragePro - Mã xác thực OTP của bạn");
            helper.setText(buildOtpEmailTemplate(otp), true);

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send OTP email to: {}", to, e);
            throw new RuntimeException("Không thể gửi email OTP", e);
        }
    }

//    public void sendServiceConfirmation(ServiceTicket ticket, boolean requiresParts, String token) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setTo(ticket.getCustomer().getEmail());
//            helper.setSubject("GaragePro - Xác nhận phiếu dịch vụ #" + ticket.getTicketId());
//            String confirmLink = "http://localhost:8080/confirm-success.html?token=" + token;
//
//            String html = buildServiceConfirmationTemplate(ticket, requiresParts, confirmLink);
//
//            helper.setText(html, true);
//
//            mailSender.send(message);
//            log.info("Service confirmation email sent to: {}", ticket.getCustomer().getEmail());
//
//        } catch (MessagingException e) {
//            log.error("Failed to send service confirmation email", e);
//            throw new RuntimeException("Không thể gửi email xác nhận dịch vụ", e);
//        }
//    }

    public String buildOtpEmailTemplate(String otp) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;
                        background: linear-gradient(135deg, #0a0a0f 0%%, #1a1a2e 100%%);
                    }
                    .container {
                        max-width: 600px;
                        margin: 40px auto;
                        background: #1E1E2E;
                        border-radius: 20px;
                        overflow: hidden;
                        box-shadow: 0 20px 60px rgba(0,0,0,0.5);
                    }
                    .header {
                        background: linear-gradient(135deg, %s, #9D4EDD);
                        padding: 40px 30px;
                        text-align: center;
                    }
                    .logo {
                        width: 80px;
                        height: 80px;
                        background: rgba(255,255,255,0.2);
                        border-radius: 20px;
                        display: inline-flex;
                        align-items: center;
                        justify-content: center;
                        font-size: 40px;
                        margin-bottom: 15px;
                        backdrop-filter: blur(10px);
                    }
                    .header h1 {
                        color: white;
                        margin: 0;
                        font-size: 28px;
                        font-weight: 700;
                    }
                    .content {
                        padding: 40px 30px;
                        color: #e0e0e0;
                    }
                    .greeting {
                        font-size: 18px;
                        margin-bottom: 20px;
                        color: white;
                    }
                    .message {
                        font-size: 15px;
                        line-height: 1.6;
                        color: #b0b0b0;
                        margin-bottom: 30px;
                    }
                    .otp-box {
                        background: linear-gradient(135deg, rgba(255,122,0,0.2), rgba(157,78,221,0.15));
                        border: 2px solid %s;
                        border-radius: 16px;
                        padding: 30px;
                        text-align: center;
                        margin: 30px 0;
                    }
                    .otp-label {
                        font-size: 14px;
                        color: #b0b0b0;
                        margin-bottom: 15px;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                    }
                    .otp-code {
                        font-size: 42px;
                        font-weight: 700;
                        color: %s;
                        letter-spacing: 8px;
                        text-shadow: 0 0 20px rgba(255,122,0,0.5);
                    }
                    .timer {
                        display: inline-block;
                        background: rgba(255,122,0,0.15);
                        color: %s;
                        padding: 8px 16px;
                        border-radius: 20px;
                        font-size: 13px;
                        margin-top: 15px;
                        font-weight: 600;
                    }
                    .warning {
                        background: rgba(239,68,68,0.15);
                        border-left: 4px solid #ef4444;
                        padding: 15px 20px;
                        border-radius: 8px;
                        margin: 20px 0;
                    }
                    .warning-text {
                        font-size: 14px;
                        color: #fca5a5;
                        margin: 0;
                    }
                    .footer {
                        background: #16161f;
                        padding: 25px 30px;
                        text-align: center;
                        border-top: 1px solid rgba(255,122,0,0.2);
                    }
                    .footer-text {
                        font-size: 13px;
                        color: #888;
                        margin: 5px 0;
                    }
                    .brand {
                        color: %s;
                        font-weight: 700;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">🔐</div>
                        <h1>Xác thực tài khoản</h1>
                    </div>
                    
                    <div class="content">
                        <div class="greeting">Xin chào! 👋</div>
                        <div class="message">
                            Cảm ơn bạn đã đăng ký tài khoản tại <strong style="color:%s">GaragePro</strong>. 
                            Để hoàn tất quá trình đăng ký, vui lòng sử dụng mã OTP dưới đây:
                        </div>
                        
                        <div class="otp-box">
                            <div class="otp-label">Mã xác thực của bạn</div>
                            <div class="otp-code">%s</div>
                            <div class="timer">⏱️ Có hiệu lực trong 3 phút</div>
                        </div>
                        
                        <div class="warning">
                            <p class="warning-text">
                                ⚠️ <strong>Lưu ý:</strong> Không chia sẻ mã này với bất kỳ ai. 
                                GaragePro sẽ không bao giờ yêu cầu mã OTP qua điện thoại hoặc email.
                            </p>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p class="footer-text">
                            Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.
                        </p>
                        <p class="footer-text">
                            © 2025 <span class="brand">GaragePro</span> - Professional Automotive Service Platform
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR,
                PRIMARY_COLOR, PRIMARY_COLOR, otp);
    }

    /**
     * Template email xác nhận dịch vụ với interactive button
     */
//    private String buildServiceConfirmationTemplate(ServiceTicket ticket, boolean requiresParts, String confirmLink) {
//        String customerName = ticket.getCustomer().getFullName();
//        String serviceName = ticket.getServiceType().getServiceName();
//        String vehicleInfo = String.format("%s %s - %s",
//                ticket.getVehicle().getBrand(),
//                ticket.getVehicle().getModel(),
//                ticket.getVehicle().getLicensePlate());
//        String expectedDate = ticket.getExpectedReturnDate() != null
//                ? ticket.getExpectedReturnDate().format(DATE_FORMATTER)
//                : "Đang cập nhật";
//        String estimateAmount = ticket.getTotalEstimate() != null
//                ? String.format("%,.0f VNĐ", ticket.getTotalEstimate())
//                : "Đang báo giá";
//
//        String partsIcon = requiresParts ? "🔧" : "🧽";
//        String partsTitle = requiresParts ? "Có sử dụng phụ tùng" : "Không cần phụ tùng";
//        String partsMessage = requiresParts
//                ? "Dịch vụ này yêu cầu sử dụng phụ tùng. Kỹ thuật viên sẽ kiểm tra và xác nhận các linh kiện cần thiết trước khi tiến hành sửa chữa."
//                : "Dịch vụ này không cần sử dụng phụ tùng. Đội ngũ kỹ thuật viên sẽ bắt đầu xử lý yêu cầu của bạn ngay sau khi bạn xác nhận.";
//
//        return """
//            <!DOCTYPE html>
//            <html>
//            <head>
//                <meta charset="UTF-8">
//                <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                <style>
//                    body {
//                        margin: 0;
//                        padding: 0;
//                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif;
//                        background: linear-gradient(135deg, #0a0a0f 0%%, #1a1a2e 100%%);
//                    }
//                    .container {
//                        max-width: 650px;
//                        margin: 40px auto;
//                        background: #1E1E2E;
//                        border-radius: 20px;
//                        overflow: hidden;
//                        box-shadow: 0 20px 60px rgba(0,0,0,0.5);
//                    }
//                    .header {
//                        background: linear-gradient(135deg, %s, #9D4EDD, #00D9FF);
//                        padding: 40px 30px;
//                        text-align: center;
//                    }
//                    .ticket-icon {
//                        width: 90px;
//                        height: 90px;
//                        background: rgba(255,255,255,0.2);
//                        border-radius: 22px;
//                        display: inline-flex;
//                        align-items: center;
//                        justify-content: center;
//                        font-size: 45px;
//                        margin-bottom: 15px;
//                        backdrop-filter: blur(10px);
//                    }
//                    .header h1 {
//                        color: white;
//                        margin: 0 0 10px 0;
//                        font-size: 28px;
//                        font-weight: 700;
//                    }
//                    .ticket-id {
//                        display: inline-block;
//                        background: rgba(255,255,255,0.2);
//                        color: white;
//                        padding: 8px 20px;
//                        border-radius: 20px;
//                        font-size: 14px;
//                        font-weight: 600;
//                        backdrop-filter: blur(10px);
//                    }
//                    .content {
//                        padding: 40px 35px;
//                        color: #e0e0e0;
//                    }
//                    .greeting {
//                        font-size: 20px;
//                        margin-bottom: 15px;
//                        color: white;
//                        font-weight: 600;
//                    }
//                    .intro {
//                        font-size: 15px;
//                        line-height: 1.6;
//                        color: #b0b0b0;
//                        margin-bottom: 30px;
//                    }
//                    .info-card {
//                        background: rgba(255,122,0,0.08);
//                        border: 1.5px solid rgba(255,122,0,0.3);
//                        border-radius: 16px;
//                        padding: 25px;
//                        margin: 25px 0;
//                    }
//                    .info-row {
//                        display: flex;
//                        justify-content: space-between;
//                        padding: 12px 0;
//                        border-bottom: 1px solid rgba(255,255,255,0.1);
//                    }
//                    .info-row:last-child {
//                        border-bottom: none;
//                    }
//                    .info-label {
//                        font-size: 14px;
//                        color: #888;
//                        font-weight: 500;
//                    }
//                    .info-value {
//                        font-size: 14px;
//                        color: white;
//                        font-weight: 600;
//                        text-align: right;
//                    }
//                    .parts-notice {
//                        background: linear-gradient(135deg, rgba(157,78,221,0.15), rgba(0,217,255,0.1));
//                        border: 1.5px solid rgba(157,78,221,0.3);
//                        border-radius: 16px;
//                        padding: 20px 25px;
//                        margin: 25px 0;
//                    }
//                    .parts-header {
//                        display: flex;
//                        align-items: center;
//                        gap: 12px;
//                        margin-bottom: 12px;
//                    }
//                    .parts-icon {
//                        font-size: 28px;
//                    }
//                    .parts-title {
//                        font-size: 16px;
//                        color: white;
//                        font-weight: 700;
//                        margin: 0;
//                    }
//                    .parts-text {
//                        font-size: 14px;
//                        line-height: 1.6;
//                        color: #b0b0b0;
//                        margin: 0;
//                    }
//                    .cta-box {
//                        background: linear-gradient(135deg, %s, #9D4EDD);
//                        border-radius: 16px;
//                        padding: 30px;
//                        text-align: center;
//                        margin: 30px 0;
//                    }
//                    .cta-title {
//                        font-size: 18px;
//                        color: white;
//                        font-weight: 700;
//                        margin-bottom: 15px;
//                    }
//                    .cta-button {
//                        display: inline-block;
//                        background: white;
//                        color: %s;
//                        padding: 14px 40px;
//                        border-radius: 12px;
//                        font-size: 16px;
//                        font-weight: 700;
//                        text-decoration: none;
//                        transition: all 0.3s;
//                        border: none;
//                        cursor: pointer;
//                    }
//                    .cta-button:hover {
//                        transform: translateY(-2px);
//                        box-shadow: 0 8px 20px rgba(255,122,0,0.3);
//                    }
//                    .cta-button.confirmed {
//                        background: linear-gradient(135deg, #10b981, #34d399);
//                        color: white;
//                        pointer-events: none;
//                    }
//                    .confirmed-icon {
//                        display: none;
//                        font-size: 50px;
//                        margin-bottom: 10px;
//                    }
//                    .confirmed-icon.show {
//                        display: block;
//                    }
//                    .loader {
//                        display: none;
//                        width: 20px;
//                        height: 20px;
//                        border: 3px solid rgba(255,122,0,0.3);
//                        border-top-color: %s;
//                        border-radius: 50%%;
//                        animation: spin 0.8s linear infinite;
//                        margin: 0 auto;
//                    }
//                    .loader.show {
//                        display: block;
//                    }
//                    @keyframes spin {
//                        to { transform: rotate(360deg); }
//                    }
//                    .footer {
//                        background: #16161f;
//                        padding: 30px 35px;
//                        border-top: 1px solid rgba(255,122,0,0.2);
//                    }
//                    .footer-title {
//                        font-size: 16px;
//                        color: white;
//                        font-weight: 600;
//                        margin-bottom: 15px;
//                    }
//                    .footer-text {
//                        font-size: 13px;
//                        color: #888;
//                        line-height: 1.6;
//                        margin: 8px 0;
//                    }
//                    .brand {
//                        color: %s;
//                        font-weight: 700;
//                    }
//                    .divider {
//                        height: 1px;
//                        background: rgba(255,255,255,0.1);
//                        margin: 20px 0;
//                    }
//                </style>
//            </head>
//            <body>
//                <div class="container">
//                    <div class="header">
//                        <div class="ticket-icon">🎫</div>
//                        <h1>Phiếu dịch vụ đã được tạo</h1>
//                        <div class="ticket-id">Mã phiếu: #%s</div>
//                    </div>
//
//                    <div class="content">
//                        <div class="greeting">Xin chào %s! 👋</div>
//                        <div class="intro">
//                            GaragePro đã tiếp nhận yêu cầu dịch vụ của bạn. Dưới đây là thông tin chi tiết:
//                        </div>
//
//                        <div class="info-card">
//                            <div class="info-row">
//                                <span class="info-label">🔧 Dịch vụ</span>
//                                <span class="info-value">%s</span>
//                            </div>
//                            <div class="info-row">
//                                <span class="info-label">🚗 Xe</span>
//                                <span class="info-value">%s</span>
//                            </div>
//                            <div class="info-row">
//                                <span class="info-label">📅 Dự kiến hoàn thành</span>
//                                <span class="info-value">%s</span>
//                            </div>
//                            <div class="info-row">
//                                <span class="info-label">💰 Chi phí dự kiến</span>
//                                <span class="info-value">%s</span>
//                            </div>
//                        </div>
//
//                        <div class="parts-notice">
//                            <div class="parts-header">
//                                <span class="parts-icon">%s</span>
//                                <h3 class="parts-title">%s</h3>
//                            </div>
//                            <p class="parts-text">%s</p>
//                        </div>
//
//                        <div class="cta-box">
//                            <div class="confirmed-icon" id="confirmedIcon">✅</div>
//                            <div class="loader" id="loader"></div>
//                            <div class="cta-title" id="ctaTitle">Bước tiếp theo</div>
//                            <a href="%s" class="cta-button" id="confirmBtn" onclick="handleConfirm(event, this)">
//                                Xác nhận phiếu dịch vụ →
//                            </a>
//                        </div>
//                    </div>
//
//                    <div class="footer">
//                        <div class="footer-title">Cần hỗ trợ?</div>
//                        <p class="footer-text">
//                            📞 Hotline: 1900-xxxx (8:00 - 20:00 hàng ngày)<br>
//                            📧 Email: support@garagepro.com<br>
//                            🌐 Website: www.garagepro.com
//                        </p>
//                        <div class="divider"></div>
//                        <p class="footer-text" style="text-align: center;">
//                            © 2025 <span class="brand">GaragePro</span> - Professional Automotive Service Platform
//                        </p>
//                    </div>
//                </div>
//
//                <script>
//                    function handleConfirm(event, button) {
//                        event.preventDefault();
//
//                        // Show loader
//                        const loader = document.getElementById('loader');
//                        const ctaTitle = document.getElementById('ctaTitle');
//                        const confirmedIcon = document.getElementById('confirmedIcon');
//
//                        button.style.display = 'none';
//                        loader.classList.add('show');
//                        ctaTitle.textContent = 'Đang xử lý...';
//
//                        // Call API
//                        fetch(button.href, {
//                            method: 'GET',
//                            headers: {
//                                'Content-Type': 'application/json'
//                            }
//                        })
//                        .then(response => {
//                            if (response.ok) {
//                                // Success
//                                setTimeout(() => {
//                                    loader.classList.remove('show');
//                                    confirmedIcon.classList.add('show');
//                                    ctaTitle.textContent = 'Xác nhận thành công!';
//                                    button.textContent = '✓ Đã xác nhận phiếu dịch vụ';
//                                    button.classList.add('confirmed');
//                                    button.style.display = 'inline-block';
//                                }, 1000);
//                            } else {
//                                throw new Error('Confirmation failed');
//                            }
//                        })
//                        .catch(error => {
//                            // Error
//                            setTimeout(() => {
//                                loader.classList.remove('show');
//                                ctaTitle.textContent = 'Có lỗi xảy ra';
//                                button.textContent = 'Thử lại';
//                                button.style.display = 'inline-block';
//                                button.style.background = 'linear-gradient(135deg, #ef4444, #dc2626)';
//                                button.style.color = 'white';
//                            }, 1000);
//                        });
//                    }
//                </script>
//            </body>
//            </html>
//            """.formatted(
//                PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR,
//                ticket.getTicketId(), customerName, serviceName, vehicleInfo,
//                expectedDate, estimateAmount, partsIcon, partsTitle, partsMessage,
//                confirmLink
//        );
//    }
}
