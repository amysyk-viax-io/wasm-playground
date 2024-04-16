package io.viax.wasmplayground.service;

import lombok.RequiredArgsConstructor;
import org.extism.sdk.HostFunction;
import org.extism.sdk.Plugin;
import org.extism.sdk.manifest.Manifest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@ConditionalOnProperty(name = "extism.plugin.execution-strategy", havingValue = "default", matchIfMissing = true)
@RequiredArgsConstructor
public class DefaultExtismPluginService implements ExtismPluginService {
    private final Manifest manifest;
    private final Supplier<HostFunction<?>[]> hostFunctionsSupplier;

    @Override
    public String call(final String fn, final String input) {
        try (final var plugin = new Plugin(this.manifest, true, this.hostFunctionsSupplier.get())) {
            return plugin.call(fn, input);
        }
    }
}
