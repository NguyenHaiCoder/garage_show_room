package fptu.edu.vn.training.utils;

import fptu.edu.vn.training.repository.OtpVerificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpCleanupScheduler {

    private final OtpVerificationRepository otpRepo;

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void cleanupExpiredOtps() {
        int updated = otpRepo.updateExpiredOtps(LocalDateTime.now());
        if (updated > 0) {
            log.info("Dọn {} OTP hết hạn", updated);
        }
    }
}
