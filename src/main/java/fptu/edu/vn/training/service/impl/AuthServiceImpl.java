package fptu.edu.vn.training.service.impl;

import fptu.edu.vn.training.config.AppProperties;
import fptu.edu.vn.training.exception.BadRequestException;
import fptu.edu.vn.training.exception.InvalidUserStatusException;
import fptu.edu.vn.training.model.entity.*;
import fptu.edu.vn.training.model.enums.*;
import fptu.edu.vn.training.model.request.*;
import fptu.edu.vn.training.model.response.AuthResponse;
import fptu.edu.vn.training.repository.*;
import fptu.edu.vn.training.security.CustomUserDetails;
import fptu.edu.vn.training.security.JwtTokenProvider;
import fptu.edu.vn.training.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userRepo;
    private final BookingRepository bookingRepo;
    private final CustomerRepository customerRepo;
    private final OtpVerificationRepository otpRepo;
    private final RoleRepository roleRepo;
    private final EmailServiceImpl emailServiceImpl;
    private final AuditLogRepository auditRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserTokenRepository tokenRepo;
    @Autowired
    private AppProperties appProperties;

    private static final int OTP_LIMIT_PER_5_MIN = 3;

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public AuthResponse register(RegisterRequest request, HttpServletRequest http) {
        String email = normalizeAndValidateEmail(request.getEmail(), true);

        long count = otpRepo.countByEmailAndCreatedAtAfter(email, LocalDateTime.now().minusMinutes(5));
        if (count >= OTP_LIMIT_PER_5_MIN) {
            throw new BadRequestException("Bạn đã yêu cầu OTP quá 3 lần trong 5 phút. Vui lòng thử lại sau.");
        }

        Optional<Users> existing = userRepo.findByEmail(email);
        if (existing.isPresent()) {
            Users user = existing.get();

            if (user.getStatus() == UserStatus.ACTIVE) {
                throw new BadRequestException("Email này đã được kích hoạt. Vui lòng đăng nhập.");
            }

            if (user.getStatus() == UserStatus.PENDING) {
                resendOtp(user, OtpAction.REGISTER, http);
                return AuthResponse.builder()
                        .message("Tài khoản chưa xác thực. Hệ thống đã gửi lại OTP mới đến email của bạn.")
                        .userId(user.getUserId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole().getRoleName().name())
                        .otpExpireAt(LocalDateTime.now().plusMinutes(3))
                        .build();
            }

            throw new InvalidUserStatusException("Tài khoản này không hợp lệ hoặc đã bị vô hiệu hoá.");
        }

        Role customerRole = roleRepo.findByRoleName(UserRole.STOCKER)
                .orElseThrow(() -> new BadRequestException("Role CUSTOMER chưa tồn tại trong DB."));

        Users user = Users.builder()
                .fullName(request.getFullName())
                .email(email)
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(UserStatus.PENDING)
                .role(customerRole)
                .build();

        userRepo.save(user);

        sendRegisterOtp(user, http);

        return AuthResponse.builder()
                .message("Đăng ký thành công! OTP đã được gửi đến email " + email)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .otpExpireAt(LocalDateTime.now().plusMinutes(3))
                .build();
    }

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public AuthResponse verifyRegisterOtp(VerifyOtpRequest request) {
        String email = normalizeAndValidateEmail(request.getEmail(), false);

        List<OtpVerification> otpList = otpRepo.findRecentByEmail(email);
        if (otpList.isEmpty()) throw new BadRequestException("Không tìm thấy OTP cho email này.");

        OtpVerification latest = otpList.get(0);
        if (latest.getExpireAt().isBefore(LocalDateTime.now())) {
            latest.setStatus(OtpStatus.EXPIRED);
            otpRepo.save(latest);
            throw new BadRequestException("OTP đã hết hạn. Vui lòng yêu cầu lại.");
        }
        if (!latest.getOtpCode().equals(request.getOtp()))
            throw new BadRequestException("Mã OTP không chính xác.");

        latest.setVerified(true);
        latest.setStatus(OtpStatus.VERIFIED);
        otpRepo.save(latest);

        Users user = latest.getUser();
        if (user.getStatus() == UserStatus.PENDING) {
            user.setStatus(UserStatus.ACTIVE);
            userRepo.save(user);
            auditRepo.save(AuditLog.builder()
                    .user(user)
                    .action("VERIFY_REGISTER_OTP_SUCCESS")
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        return AuthResponse.builder()
                .message("Xác thực thành công. Tài khoản của bạn đã được kích hoạt. Vui lòng đăng nhập.")
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .build();
    }

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public AuthResponse requestLoginOtp(LoginOtpRequest request, HttpServletRequest http) {
        String email = normalizeAndValidateEmail(request.getEmail(), false);

        Users user = userRepo.findByEmail(email).orElseGet(() -> {
            BookingCustomerInfo bookingInfo = bookingRepo.findCustomerInfoByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Email chưa tồn tại trong hệ thống."));

            Role customerRole = roleRepo.findByRoleName(UserRole.CUSTOMER)
                    .orElseThrow(() -> new BadRequestException("Role CUSTOMER chưa tồn tại trong DB."));

            Users newUser = Users.builder()
                    .fullName(bookingInfo.getFullName())
                    .email(bookingInfo.getEmail())
                    .phone(bookingInfo.getPhone())
                    .status(UserStatus.ACTIVE)
                    .role(customerRole)
                    .build();
            userRepo.save(newUser);

            Customer customer = customerRepo.findById(bookingInfo.getCustomerId())
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy customer từ booking."));
            customer.setUser(newUser);
            customer.setIsDeleted(0);
            customerRepo.save(customer);

            return newUser;
        });

        if (user.getStatus() != UserStatus.ACTIVE)
            throw new InvalidUserStatusException("Tài khoản chưa được kích hoạt hoặc đã bị khóa.");

        long count = otpRepo.countByEmailAndCreatedAtAfter(email, LocalDateTime.now().minusMinutes(5));
        if (count >= OTP_LIMIT_PER_5_MIN)
            throw new BadRequestException("Bạn đã yêu cầu OTP quá 3 lần trong 5 phút.");

        String otp = genOtp();
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(3);

        try {
            otpRepo.save(OtpVerification.builder()
                    .user(user)
                    .email(email)
                    .otpCode(otp)
                    .expireAt(expireAt)
                    .verified(false)
                    .status(OtpStatus.NEW)
                    .action(OtpAction.LOGIN)
                    .ipAddress(http.getRemoteAddr())
                    .createdAt(LocalDateTime.now())
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Không thể tạo OTP do dữ liệu không hợp lệ.");
        }

        emailServiceImpl.sendOtpEmail(email, otp);

        auditRepo.save(AuditLog.builder()
                .user(user)
                .action("SEND_LOGIN_OTP")
                .createdAt(LocalDateTime.now())
                .build());

        return AuthResponse.builder()
                .message("OTP đăng nhập đã được gửi đến email " + email)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .otpExpireAt(expireAt)
                .build();
    }

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public AuthResponse verifyLoginOtp(VerifyOtpRequest request) {
        String email = normalizeAndValidateEmail(request.getEmail(), false);

        Users user = userRepo.findByEmail(email).orElseGet(() -> {
            BookingCustomerInfo bookingInfo = bookingRepo.findCustomerInfoByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Email chưa tồn tại trong hệ thống."));

            Role customerRole = roleRepo.findByRoleName(UserRole.CUSTOMER)
                    .orElseThrow(() -> new BadRequestException("Role CUSTOMER chưa tồn tại trong DB."));

            Users newUser = Users.builder()
                    .fullName(bookingInfo.getFullName())
                    .email(bookingInfo.getEmail())
                    .phone(bookingInfo.getPhone())
                    .status(UserStatus.ACTIVE)
                    .role(customerRole)
                    .build();
            userRepo.save(newUser);

            Customer customer = customerRepo.findById(bookingInfo.getCustomerId())
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy customer từ booking."));
            customer.setUser(newUser);
            customer.setIsDeleted(0);
            customerRepo.save(customer);

            return newUser;
        });

        if (user.getStatus() != UserStatus.ACTIVE)
            throw new InvalidUserStatusException("Tài khoản của bạn chưa được kích hoạt hoặc đã bị khóa.");

        List<OtpVerification> otpList = otpRepo.findRecentByEmail(email);
        if (otpList.isEmpty()) throw new BadRequestException("Không tìm thấy OTP cho email này.");

        OtpVerification latest = otpList.get(0);
        if (latest.getExpireAt().isBefore(LocalDateTime.now())) {
            latest.setStatus(OtpStatus.EXPIRED);
            otpRepo.save(latest);
            resendOtp(user, OtpAction.LOGIN, null);
            throw new BadRequestException("OTP đã hết hạn. Hệ thống đã gửi lại OTP mới đến email của bạn.");
        }
        if (!latest.getOtpCode().equals(request.getOtp()))
            throw new BadRequestException("Mã OTP không chính xác.");

        latest.setVerified(true);
        latest.setStatus(OtpStatus.VERIFIED);
        otpRepo.save(latest);

        revokeAllUserTokens(user.getUserId());

        UserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        saveUserToken(user, accessToken, TokenType.ACCESS, 15 * 60 * 1000);
        saveUserToken(user, refreshToken, TokenType.REFRESH, 7 * 24 * 60 * 60 * 1000);

        auditRepo.save(AuditLog.builder()
                .user(user)
                .action("LOGIN_SUCCESS")
                .createdAt(LocalDateTime.now())
                .build());

        return AuthResponse.builder()
                .message("Đăng nhập thành công! Tài khoản đã được xác thực.")
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtTokenProvider.extractUsername(refreshToken);
        Users user = userRepo.findByEmail(email).orElseGet(() -> {
            BookingCustomerInfo bookingInfo = bookingRepo.findCustomerInfoByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Email chưa tồn tại trong hệ thống."));

            Role customerRole = roleRepo.findByRoleName(UserRole.CUSTOMER)
                    .orElseThrow(() -> new BadRequestException("Role CUSTOMER chưa tồn tại trong DB."));

            Users newUser = Users.builder()
                    .fullName(bookingInfo.getFullName())
                    .email(bookingInfo.getEmail())
                    .phone(bookingInfo.getPhone())
                    .status(UserStatus.ACTIVE)
                    .role(customerRole)
                    .build();
            userRepo.save(newUser);

            Customer customer = customerRepo.findById(bookingInfo.getCustomerId())
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy customer từ booking."));
            customer.setUser(newUser);
            customer.setIsDeleted(0);
            customerRepo.save(customer);

            return newUser;
        });

        UserDetails userDetails = new CustomUserDetails(user);
        if (!jwtTokenProvider.isTokenValid(refreshToken, userDetails))
            throw new BadRequestException("Refresh token hết hạn hoặc không hợp lệ.");

        revokeAllUserTokens(user.getUserId());

        String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        saveUserToken(user, newAccessToken, TokenType.ACCESS, 15 * 60 * 1000);
        saveUserToken(user, newRefreshToken, TokenType.REFRESH, 7 * 24 * 60 * 60 * 1000);

        log.info("Refresh token thành công cho user [{}]", email);

        return AuthResponse.builder()
                .message("Làm mới token thành công!")
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public void logout(String token) {
        if (token == null || token.trim().isEmpty())
            throw new BadRequestException("Token không hợp lệ hoặc rỗng.");

        String email;
        try {
            email = jwtTokenProvider.extractUsername(token);
        } catch (Exception e) {
            throw new BadRequestException("Token không hợp lệ hoặc đã bị chỉnh sửa.");
        }

        Users user = userRepo.findByEmail(email).orElseGet(() -> {
            BookingCustomerInfo bookingInfo = bookingRepo.findCustomerInfoByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Email chưa tồn tại trong hệ thống."));

            Role customerRole = roleRepo.findByRoleName(UserRole.CUSTOMER)
                    .orElseThrow(() -> new BadRequestException("Role CUSTOMER chưa tồn tại trong DB."));

            Users newUser = Users.builder()
                    .fullName(bookingInfo.getFullName())
                    .email(bookingInfo.getEmail())
                    .phone(bookingInfo.getPhone())
                    .status(UserStatus.ACTIVE)
                    .role(customerRole)
                    .build();
            userRepo.save(newUser);

            Customer customer = customerRepo.findById(bookingInfo.getCustomerId())
                    .orElseThrow(() -> new BadRequestException("Không tìm thấy customer từ booking."));
            customer.setUser(newUser);
            customer.setIsDeleted(0);
            customerRepo.save(customer);

            return newUser;
        });

        UserDetails userDetails = new CustomUserDetails(user);
        if (!jwtTokenProvider.isTokenValid(token, userDetails))
            throw new BadRequestException("Token không hợp lệ hoặc đã hết hạn.");

        revokeAllUserTokens(user.getUserId());

        auditRepo.save(AuditLog.builder()
                .user(user)
                .action("LOGOUT_SUCCESS")
                .createdAt(LocalDateTime.now())
                .build());

        log.info("User [{}] đã logout thành công.", email);
    }

    @Override
    @Transactional(noRollbackFor = {BadRequestException.class, InvalidUserStatusException.class})
    public void resendOtp(Users user, OtpAction action, HttpServletRequest http) {
        String email = user.getEmail();

        OtpVerification latest = otpRepo.findLatestByUserAndEmail(user, email).orElse(null);
        if (latest != null && latest.getStatus() != OtpStatus.VERIFIED) {
            latest.setStatus(OtpStatus.EXPIRED);
            otpRepo.save(latest);
        }

        String otp = genOtp();
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(3);

        try {
            otpRepo.save(OtpVerification.builder()
                    .user(user)
                    .email(email)
                    .otpCode(otp)
                    .expireAt(expireAt)
                    .verified(false)
                    .status(OtpStatus.RESENT)
                    .action(action)
                    .ipAddress(http != null ? http.getRemoteAddr() : "system")
                    .createdAt(LocalDateTime.now())
                    .build());
        } catch (DataIntegrityViolationException e) {
            log.error("Lỗi CSDL khi resend OTP: {}", e.getMessage());
            throw new BadRequestException("Không thể tạo OTP do dữ liệu không hợp lệ.");
        }

        emailServiceImpl.sendOtpEmail(email, otp);

        String logAction = (action == OtpAction.LOGIN) ? "RESEND_LOGIN_OTP" : "RESEND_REGISTER_OTP";
        auditRepo.save(AuditLog.builder()
                .user(user)
                .action(logAction)
                .createdAt(LocalDateTime.now())
                .build());
    }

    private void sendRegisterOtp(Users user, HttpServletRequest http) {
        String otp = genOtp();
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(3);

        otpRepo.save(OtpVerification.builder()
                .user(user)
                .email(user.getEmail())
                .otpCode(otp)
                .expireAt(expireAt)
                .verified(false)
                .status(OtpStatus.NEW)
                .action(OtpAction.REGISTER)
                .ipAddress(http.getRemoteAddr())
                .createdAt(LocalDateTime.now())
                .build());

        emailServiceImpl.sendOtpEmail(user.getEmail(), otp);
        auditRepo.save(AuditLog.builder()
                .user(user)
                .action("SEND_REGISTER_OTP")
                .createdAt(LocalDateTime.now())
                .build());
    }

    private String normalizeAndValidateEmail(String raw, boolean checkDomainAllowList) {
        if (!StringUtils.hasText(raw)) {
            throw new BadRequestException("Email không được để trống.");
        }
        String email = raw.trim().toLowerCase();

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BadRequestException("Định dạng email không hợp lệ.");
        }

        if (checkDomainAllowList) {
            String domain = email.substring(email.lastIndexOf('@') + 1);
            List<String> allow = appProperties.getAllowedEmailDomains();
            if (allow != null && !allow.isEmpty()) {
                boolean ok = allow.stream().anyMatch(d -> domain.equalsIgnoreCase(d.trim()));
                if (!ok) {
                    String allowText = allow.stream()
                            .map(String::trim)
                            .filter(StringUtils::hasText)
                            .collect(Collectors.joining(", "));
                    throw new BadRequestException(
                            "Tên miền email không được phép. Cho phép: " + allowText + "."
                    );
                }
            }
        }
        return email;
    }

    private void saveUserToken(Users user, String tokenValue, TokenType type, long durationMs) {
        if (!StringUtils.hasText(tokenValue))
            throw new IllegalArgumentException(type + " token không được rỗng.");

        LocalDateTime now = LocalDateTime.now();
        UserToken token = UserToken.builder()
                .user(user)
                .type(type)
                .status(TokenStatus.ACTIVE)
                .createdAt(now)
                .expiresAt(now.plusSeconds(durationMs / 1000))
                .build();

        if (type == TokenType.ACCESS) token.setAccessToken(tokenValue);
        else token.setRefreshToken(tokenValue);

        tokenRepo.save(token);
    }

    private void revokeAllUserTokens(Integer userId) {
        List<UserToken> activeTokens = tokenRepo.findAllByUser_UserIdAndStatus(userId, TokenStatus.ACTIVE);
        activeTokens.forEach(t -> t.setStatus(TokenStatus.REVOKED));
        tokenRepo.saveAll(activeTokens);
    }

    private String genOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}

