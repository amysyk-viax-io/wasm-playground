package io.viax.wasmplayground.controller;

import io.viax.wasmplayground.model.SimpleModel;
import io.viax.wasmplayground.service.ExtismPluginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExtismController {
    private static final String KV_READ_FN = "kvStoreRead";
    private static final String KV_WRITE_FN = "kvStoreWrite";
    private static final String SIMPLE_HTTP_POST_FN = "simpleHttpPost";

    private final ExtismPluginService extismPluginService;

    @PostMapping("/extism/http-post")
    public String httpPost(@RequestBody final SimpleModel model) {
        return this.extismPluginService.invoke(plugin -> plugin.call(SIMPLE_HTTP_POST_FN, model.getId()));
    }

    @PostMapping("/extism/kv-store")
    public String kvStore(@RequestBody final SimpleModel model) {
        return this.extismPluginService.invoke(plugin -> {
            plugin.call(KV_WRITE_FN, model.getId());
            return plugin.call(KV_READ_FN, model.getId());
        });
    }
}
