package io.viax.wasmplayground.configuration;

import lombok.RequiredArgsConstructor;
import org.extism.sdk.ExtismFunction;
import org.extism.sdk.HostFunction;
import org.extism.sdk.HostUserData;
import org.extism.sdk.LibExtism;
import org.extism.sdk.manifest.Manifest;
import org.extism.sdk.manifest.MemoryOptions;
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
import java.util.Optional;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class ExtismConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ExtismConfiguration.class);

    private final ExtismPluginProperties pluginProperties;
    private final Map<String, String> kvStore;

    @Bean
    public WasmSourceResolver wasmSourceResolver() {
        return new WasmSourceResolver();
    }

    @Bean
    public WasmSource wasmSource() {
        return this.wasmSourceResolver().resolve(Path.of(this.pluginProperties.getPath()));
    }

    @Bean
    public MemoryOptions memoryOptions() {
        return new MemoryOptions(32);
    }

    @Bean
    public Manifest manifest() {
        final List<WasmSource> sources = List.of(this.wasmSource());
        return new Manifest(sources, this.memoryOptions(), null, this.pluginProperties.getAllowedHosts());
    }

    @Bean
    public ExtismFunction<HostUserData> kvWriteFn() {
        return (plugin, params, returns, data) -> {
            final var key = plugin.inputString(params[0]);
            final var value = plugin.inputString(params[1]);

            log.debug("kvWriteFn key[{}], value[{}]", key, value);

            this.kvStore.put(key, value);
            plugin.returnString(returns[0], key);
        };
    }

    @Bean
    public ExtismFunction<HostUserData> kvReadFn() {
        return (plugin, params, returns, data) -> {
            final var key = plugin.inputString(params[0]);
            final var value = this.kvStore.get(key);

            log.debug("kvReadFn key[{}], value[{}]", key, value);

            plugin.returnString(returns[0], Objects.requireNonNullElse(value, ""));
        };
    }

    @Bean
    public Supplier<HostFunction<?>[]> hostFunctionsSupplier() {
        return () -> new HostFunction[]{
                new HostFunction<>(
                        "kv_read",
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64},
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64},
                        this.kvReadFn(),
                        Optional.empty()
                ),
                new HostFunction<>(
                        "kv_write",
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64, LibExtism.ExtismValType.I64},
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64},
                        this.kvWriteFn(),
                        Optional.empty()
                )
        };
    }
}
