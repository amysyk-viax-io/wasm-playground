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

    private final ExtismPluginService extismPluginService;

    @PostMapping("/extism/http-post")
    public String httpPost(@RequestBody final SimpleModel model) {
        return this.extismPluginService.call("simpleHttpPost", model.getId());
    }

    @PostMapping("/extism/kv-store")
    public String kvStore(@RequestBody final SimpleModel model) {
        this.extismPluginService.call("kvStoreWrite", model.getId());
        return this.extismPluginService.call("kvStoreRead", model.getId());
    }
}
