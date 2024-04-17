package io.viax.wasmplayground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.extism.sdk.HostFunction;
import org.extism.sdk.Plugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Service
@ConditionalOnProperty(name = "extism.plugin.execution-strategy", havingValue = "default", matchIfMissing = true)
@RequiredArgsConstructor
public class DefaultExtismPluginService implements ExtismPluginService {
    private final byte[] manifest;
    private final Supplier<HostFunction<?>[]> hostFunctionsSupplier;

    @Override
    public String invoke(final Function<Plugin, String> fn) {
        try (final var plugin = new Plugin(this.manifest, true, this.hostFunctionsSupplier.get())) {
            return fn.apply(plugin);
        }
    }
}
