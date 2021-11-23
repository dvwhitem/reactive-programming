package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;


@Slf4j
public class FluxTest {


    @Test
    public void sequenceFlux() throws InterruptedException {

        Flux.range(2018, 7)
                .timestamp()
                .index()
                .subscribe(e -> log.info(
                        "index {} timestamp: {} value: {}",
                        e.getT1(),
                        Instant.ofEpochMilli(e.getT2().getT1()),
                        e.getT2().getT2()
                        )
                );
    }

    @Test
    public void startStopStreamProcessing() throws InterruptedException {
        Mono<Long> startCommand = Mono.delay(Duration.ofSeconds(1));
        Mono<Long> stopCommand = Mono.delay(Duration.ofSeconds(3));

        Flux<Long> streamOfData = Flux.interval(Duration.ofMillis(100));

        streamOfData
                .skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(m -> log.info("Message: {}", m));

        Thread.sleep(10000);

    }
}
