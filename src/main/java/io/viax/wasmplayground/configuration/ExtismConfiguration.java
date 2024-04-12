package io.viax.wasmplayground.configuration;

import lombok.RequiredArgsConstructor;
import org.extism.sdk.ExtismFunction;
import org.extism.sdk.HostUserData;
import org.extism.sdk.manifest.Manifest;
import org.extism.sdk.wasm.WasmSource;
import org.extism.sdk.wasm.WasmSourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class ExtismConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ExtismConfiguration.class);

    private final ExtismPluginProperties pluginProperties;

    @Bean
    public WasmSourceResolver wasmSourceResolver() {
        return new WasmSourceResolver();
    }

    @Bean
    public WasmSource wasmSource() {
        return this.wasmSourceResolver().resolve(Path.of(this.pluginProperties.getPath()));
    }

    @Bean
    public Manifest manifest() {
        final List<WasmSource> sources = List.of(this.wasmSource());
        return new Manifest(sources, null, null, this.pluginProperties.getAllowedHosts());
    }

    @Bean
    public Map<String, String> simpleKvStore() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ExtismFunction<HostUserData> simpleKvStoreWriteFn() {
        return (plugin, params, returns, data) -> {
            final var key = plugin.inputString(params[0]);
            final var value = plugin.inputString(params[1]);

            log.debug("simpleKvStoreWriteFn key[{}], value[{}]", key, value);

            this.simpleKvStore().put(key, value);
            plugin.returnString(returns[0], key);
        };
    }

    @Bean
    public ExtismFunction<HostUserData> simpleKvStoreReadFn() {
        return (plugin, params, returns, data) -> {
            final var key = plugin.inputString(params[0]);
            final var value = this.simpleKvStore().get(key);

            log.debug("simpleKvStoreReadFn key[{}], value[{}]", key, value);

            plugin.returnString(returns[0], Objects.requireNonNullElse(value, ""));
        };
    }
}
