package io.viax.wasmplayground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.extism.sdk.HostFunction;
import org.extism.sdk.Plugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Service
@ConditionalOnProperty(name = "extism.plugin.execution-strategy", havingValue = "default", matchIfMissing = true)
@RequiredArgsConstructor
public class DefaultExtismPluginService implements ExtismPluginService {
    private final byte[] manifest;
    private final Supplier<HostFunction<?>[]> hostFunctionsSupplier;
    private final ExecutorService closeExecutor = Executors.newSingleThreadExecutor();

    @Override
    public String invoke(final Function<Plugin, String> fn) {
        final var initStartTime = System.nanoTime();
        final var plugin = new Plugin(this.manifest, true, this.hostFunctionsSupplier.get());
        final var initEndTime = System.nanoTime();

        log.debug("init time[{}ms]", ((initEndTime - initStartTime) / 1000000D));

        try {
            return fn.apply(plugin);
        } finally {
            CompletableFuture.runAsync(() -> {
                final var closeStartTime = System.nanoTime();
                plugin.close();
                final var closeEndTime = System.nanoTime();

                log.debug("close time[{}ms]", ((closeEndTime - closeStartTime) / 1000000D));
            }, this.closeExecutor);
        }
    }
}
