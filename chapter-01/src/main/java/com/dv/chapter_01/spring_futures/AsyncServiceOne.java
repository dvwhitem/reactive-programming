package com.dv.chapter_01.spring_futures;

import com.dv.chapter_01.commons.Example;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.Future;

@RestController
@RequestMapping("api/v2/resource/a")
@Slf4j
public class AsyncServiceOne {
    private static final String PORT = "8082";

    @GetMapping
    public Future<?> process() {
        AsyncRestTemplate template = new AsyncRestTemplate(); // deprecated
        SuccessCallback onSuccess = s -> log.info("Success");
        FailureCallback onFailure = e -> log.info("Failure");

        ListenableFuture<?> response = template.getForEntity(
                "http://localhost:" + PORT + "/api/v2/resource/b",
                Example.class
        );

        response.addCallback(onSuccess, onFailure);
        return response;
    }
}
