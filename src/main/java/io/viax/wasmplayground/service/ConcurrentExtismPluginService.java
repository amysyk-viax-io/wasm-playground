package io.viax.wasmplayground.service;

import org.extism.sdk.ExtismFunction;
import org.extism.sdk.HostFunction;
import org.extism.sdk.HostUserData;
import org.extism.sdk.LibExtism;
import org.extism.sdk.Plugin;
import org.extism.sdk.manifest.Manifest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@ConditionalOnProperty(name = "extism.plugin.strategy", havingValue = "concurrent")
public class ConcurrentExtismPluginService implements ExtismPluginService {
    private final ExtismFunction<HostUserData> simpleKvStoreReadFn;
    private final ExtismFunction<HostUserData> simpleKvStoreWriteFn;

    private final ThreadLocal<Plugin> plugin;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public ConcurrentExtismPluginService(final Manifest manifest, final ExtismFunction<HostUserData> simpleKvStoreReadFn, final ExtismFunction<HostUserData> simpleKvStoreWriteFn) {
        this.simpleKvStoreReadFn = simpleKvStoreReadFn;
        this.simpleKvStoreWriteFn = simpleKvStoreWriteFn;

        this.plugin = ThreadLocal.withInitial(() -> new Plugin(manifest, true, this.getHostFunctions()));
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

    private HostFunction<?>[] getHostFunctions() {
        return new HostFunction[]{
                new HostFunction<>(
                        "kv_read",
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64},
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64},
                        this.simpleKvStoreReadFn,
                        Optional.empty()
                ),
                new HostFunction<>(
                        "kv_write",
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64, LibExtism.ExtismValType.I64},
                        new LibExtism.ExtismValType[]{LibExtism.ExtismValType.I64},
                        this.simpleKvStoreWriteFn,
                        Optional.empty()
                )
        };
    }
}
