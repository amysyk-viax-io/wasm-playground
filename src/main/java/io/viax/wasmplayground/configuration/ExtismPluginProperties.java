package io.viax.wasmplayground.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties("extism.plugin")
public class ExtismPluginProperties {
    private String path;
    private ExecutionStrategy executionStrategy = ExecutionStrategy.DEFAULT;
    private List<String> allowedHosts = new ArrayList<>();

    private enum ExecutionStrategy {
        DEFAULT, CONCURRENT
    }
}
