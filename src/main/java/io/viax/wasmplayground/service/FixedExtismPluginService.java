package io.viax.wasmplayground.service;

import org.extism.sdk.HostFunction;
import org.extism.sdk.Plugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@ConditionalOnProperty(name = "extism.plugin.execution-strategy", havingValue = "fixed")
public class FixedExtismPluginService implements ExtismPluginService {
    private final ThreadLocal<Plugin> plugin;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public FixedExtismPluginService(final byte[] manifest, final Supplier<HostFunction<?>[]> hostFunctionsSupplier) {
        this.plugin = ThreadLocal.withInitial(() -> new Plugin(manifest, true, hostFunctionsSupplier.get()));
    }

    @Override
    public String invoke(final Function<Plugin, String> fn) {
        final var future = this.executor.submit(() -> fn.apply(this.plugin.get()));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
