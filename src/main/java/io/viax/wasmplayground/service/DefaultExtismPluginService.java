package io.viax.wasmplayground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.extism.sdk.HostFunction;
import org.extism.sdk.Plugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Slf4j
@Service
@ConditionalOnProperty(name = "extism.plugin.execution-strategy", havingValue = "default", matchIfMissing = true)
@RequiredArgsConstructor
public class DefaultExtismPluginService implements ExtismPluginService {
    private final byte[] manifest;
    private final Supplier<HostFunction<?>[]> hostFunctionsSupplier;

    @Override
    public String call(final String fn, final String input) {
        try (final var plugin = new Plugin(this.manifest, true, this.hostFunctionsSupplier.get())) {
            final var startTime = System.nanoTime();
            final var result = plugin.call(fn, input);
            final var endTime = System.nanoTime();

            log.debug("call fn[{}], input[{}], time[{}ms]", fn, input, ((endTime - startTime) / 1000000D));

            return result;
        }
    }
}
