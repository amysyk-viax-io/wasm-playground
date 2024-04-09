package io.viax.wasmplayground.service;

import lombok.RequiredArgsConstructor;
import org.extism.sdk.ExtismFunction;
import org.extism.sdk.HostFunction;
import org.extism.sdk.HostUserData;
import org.extism.sdk.LibExtism;
import org.extism.sdk.Plugin;
import org.extism.sdk.manifest.Manifest;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class DefaultExtismPluginService implements ExtismPluginService {
    private final Manifest manifest;
    private final ExtismFunction<HostUserData> simpleKvStoreReadFn;
    private final ExtismFunction<HostUserData> simpleKvStoreWriteFn;

    @Override
    public String call(final String fn, final String input) {
        try (final var plugin = new Plugin(this.manifest, true, this.getHostFunctions())) {
            return plugin.call(fn, input);
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
