//package fptu.edu.vn.training.config;
//
//import fptu.edu.vn.training.model.entity.Role;
//import fptu.edu.vn.training.model.enums.UserRole;
//import fptu.edu.vn.training.model.enums.UserStatus;
//import fptu.edu.vn.training.repository.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class DatabaseSeeder {
//
//    private final RoleRepository roleRepository;
//
//    @Bean
//    @Transactional
//    CommandLineRunner initDatabase() {
//        return args -> {
//            log.info("Bắt đầu khởi tạo dữ liệu Role...");
//
//            var rolesToSeed = List.of(
//                    new RoleSeedData(UserRole.ADMIN,     "Quản trị hệ thống toàn quyền"),
//                    new RoleSeedData(UserRole.MANAGER,   "Quản lý chung, xem báo cáo & phân công công việc"),
//                    new RoleSeedData(UserRole.STAFF,     "Nhân viên lễ tân/thu ngân, tiếp nhận khách và tạo phiếu dịch vụ"),
//                    new RoleSeedData(UserRole.MECHANIC,  "Thợ sửa chữa, thực hiện công việc bảo dưỡng và cập nhật tiến độ"),
//                    new RoleSeedData(UserRole.CUSTOMER,  "Khách hàng sử dụng dịch vụ và theo dõi phiếu sửa chữa")
//            );
//
//            long created = 0;
//            long skipped = 0;
//
//            for (var data : rolesToSeed) {
//                if (roleRepository.findByRoleName(data.roleName()).isEmpty()) {
//                    // Tạo mới
//                    Role role = Role.builder()
//                            .roleId(UUID.randomUUID().toString())
//                            .roleName(data.roleName())           // ← enum nguyên bản
//                            .description(data.description())
//                            .build();
//
//                    roleRepository.save(role);
//                    log.info("Đã tạo Role: {} - {}", data.roleName(), data.description());
//                    created++;
//                } else {
//                    log.debug("Role đã tồn tại: {}", data.roleName());
//                    skipped++;
//                }
//            }
//
//            log.info("Hoàn tất khởi tạo Role: {} mới tạo, {} đã tồn tại", created, skipped);
//        };
//    }
//
//    // Record ngắn gọn, đẹp đẽ (Java 14+)
//    private record RoleSeedData(UserRole roleName, String description) {}
//}