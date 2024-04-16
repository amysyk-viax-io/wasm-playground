package io.viax.wasmplayground.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class KeyValueStoreConfiguration {

    @Bean
    public Map<String, String> kvStore() {
        return new ConcurrentHashMap<>();
    }
}
