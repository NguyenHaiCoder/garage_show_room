package fptu.edu.vn.training.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "spring.mail")
public record AppMailProperties(
        @NotBlank String host,
        @Positive int port,
        String username,
        String password,
        String defaultEncoding,
        Smtp smtp,
        Map<String, String> properties
) {
    public AppMailProperties {
        if (defaultEncoding == null || defaultEncoding.isBlank()) {
            defaultEncoding = "UTF-8";
        }
        if (smtp == null) {
            smtp = new Smtp(false, false, "localhost");
        }
    }

    public record Smtp(
            boolean auth,
            boolean starttlsEnable,
            String sslTrust
    ) {}
}
