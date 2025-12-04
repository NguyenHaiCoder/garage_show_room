package fptu.edu.vn.training.config;

import fptu.edu.vn.training.config.props.AppMailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(AppMailProperties.class)
public class MailConfig {

    private final AppMailProperties props;

    public MailConfig(AppMailProperties props) {
        this.props = props;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(props.host());
        mailSender.setPort(props.port());
        mailSender.setUsername(props.username());
        mailSender.setPassword(props.password());
        mailSender.setDefaultEncoding(props.defaultEncoding());

        Properties p = mailSender.getJavaMailProperties();
        p.put("mail.smtp.auth", props.smtp().auth());
        p.put("mail.smtp.starttls.enable", props.smtp().starttlsEnable());
        p.put("mail.smtp.ssl.trust", props.smtp().sslTrust());
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.debug", "false");

        if (props.properties() != null) {
            p.putAll(props.properties());
        }

        return mailSender;
    }
}
