package fptu.edu.vn.training.utils;

import fptu.edu.vn.training.model.entity.AuditLog;
import fptu.edu.vn.training.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditLogUtils {

    private final AuditLogRepository auditRepo;
    private final AuthUtils authUtils;

    public void log(String action) {
        AuditLog log = AuditLog.builder()
                .user(authUtils.getCurrentUser())
                .action(action)
                .createdAt(LocalDateTime.now())
                .build();

        auditRepo.save(log);
    }
}

