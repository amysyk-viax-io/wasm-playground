package io.viax.wasmplayground.service;

import org.extism.sdk.Plugin;

import java.util.function.Function;

public interface ExtismPluginService {

    String invoke(Function<Plugin, String> fn);
}
