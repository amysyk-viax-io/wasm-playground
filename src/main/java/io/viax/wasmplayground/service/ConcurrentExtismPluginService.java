package io.viax.wasmplayground.service;

import org.extism.sdk.HostFunction;
import org.extism.sdk.Plugin;
import org.extism.sdk.manifest.Manifest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Service
@ConditionalOnProperty(name = "extism.plugin.execution-strategy", havingValue = "concurrent")
public class ConcurrentExtismPluginService implements ExtismPluginService {
    private final ThreadLocal<Plugin> plugin;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public ConcurrentExtismPluginService(final Manifest manifest, final Supplier<HostFunction<?>[]> hostFunctionsSupplier) {
        this.plugin = ThreadLocal.withInitial(() -> new Plugin(manifest, true, hostFunctionsSupplier.get()));
    }

    @Override
    public String call(final String fn, final String input) {
        final var future = this.executor.submit(() -> this.plugin.get().call(fn, input));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
