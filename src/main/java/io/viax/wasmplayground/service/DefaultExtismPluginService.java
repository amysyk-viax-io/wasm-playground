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
        double initTime;
        double executionTime = 0;
        double closeTime;

        final var initStartTime = System.nanoTime();
        final var plugin = new Plugin(this.manifest, true, this.hostFunctionsSupplier.get());
        final var initEndTime = System.nanoTime();

        initTime = (initEndTime - initStartTime) / 1000000D;

        try {
            final var executionStartTime = System.nanoTime();
            final var result = fn.apply(plugin);
            final var executionEndTime = System.nanoTime();

            executionTime = (executionEndTime - executionStartTime) / 1000000D;

            return result;
        } finally {
            final var closeStartTime = System.nanoTime();
            plugin.close();
            final var closeEndTime = System.nanoTime();

            closeTime = (closeEndTime - closeStartTime) / 1000000D;

            log.debug("Plugin: new[{}ms] call[{}ms] close[{}ms]", initTime, executionTime, closeTime);
        }
    }
}
