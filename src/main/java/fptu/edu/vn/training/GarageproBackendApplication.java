package fptu.edu.vn.training;

import fptu.edu.vn.training.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(AppProperties.class)
public class GarageproBackendApplication {

    public static void main(String[] args) {
        System.setProperty("spring.backgroundpreinitializer.ignore", "true");
        System.setProperty("spring.jmx.enabled", "false");

        SpringApplication app = new SpringApplication(GarageproBackendApplication.class);
        app.setLogStartupInfo(true);
        app.setRegisterShutdownHook(false);

        var context = app.run(args);

        String port = context.getEnvironment().getProperty("server.port");

        System.out.println("\nGaragePro Backend đã sẵn sàng!");
        System.out.println("http://localhost:" + port);
        System.out.println("Startup completed!\n");
    }
}
