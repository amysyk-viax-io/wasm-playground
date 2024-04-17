package io.viax.wasmplayground.controller;

import io.viax.wasmplayground.model.SimpleModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class SimpleController {

    @PostMapping("/simple/some-processing")
    public String someProcessing(@RequestBody final SimpleModel model, @RequestHeader final Map<String, String> headers) throws InterruptedException {
        log.debug("processing body[{}], headers[{}]", model, headers);
//        Thread.sleep(100);
        return "processing for " + model.getId();
    }
}
