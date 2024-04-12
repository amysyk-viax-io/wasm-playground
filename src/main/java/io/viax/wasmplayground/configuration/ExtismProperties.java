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
@ConfigurationProperties("extism")
public class ExtismProperties {
    private String pluginPath;
    private List<String> allowedHosts = new ArrayList<>();
}
