package fptu.edu.vn.training.config;

import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UndertowConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    @Override
    public void customize(UndertowServletWebServerFactory factory) {

        // ⚡ Tối ưu buffer và số lượng thread xử lý IO
        factory.setIoThreads(Math.max(2, Runtime.getRuntime().availableProcessors()));
        factory.setWorkerThreads(50);
        factory.setBufferSize(1024);
        factory.setUseDirectBuffers(true);

        factory.addBuilderCustomizers(builder -> {
            builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            builder.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false);
            builder.setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT, 15000);
        });
    }
}
