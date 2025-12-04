package fptu.edu.vn.training.security;

import fptu.edu.vn.training.model.entity.Users;
import fptu.edu.vn.training.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserAccountRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));

        if (user.getStatus() == null || !user.getStatus().name().equalsIgnoreCase("ACTIVE")) {
            throw new UsernameNotFoundException("Tài khoản của bạn chưa được kích hoạt hoặc đã bị khóa");
        }

        return new CustomUserDetails(user);
    }
}
