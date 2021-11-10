package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MonoReactorTest {

    @Test
    public void createMono() {
        var stringMono = Mono.<String>justOrEmpty(Optional.empty()).log();

        StepVerifier
                .create(stringMono)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void createMono2() {
        var intMono = Mono.just(987);

        StepVerifier.create(intMono.log())
                .expectSubscription()
                .expectNext(987)
                .verifyComplete();
    }

    @Test
    public void createMono3() {

        var mono = Mono.<Void>fromRunnable(this::doLongAction);

        StepVerifier.create(mono)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void convertMonoToFlux() {
        var list = Arrays.asList("one", "two", "three", "four");
        var monoList = Mono.just(list).log();

        StepVerifier.create(monoList)
                .expectSubscription()
                .expectNext(List.of("one", "two", "three", "four"))
                .verifyComplete();

        log.info("--------------------------");

        var fluxIterable = monoList.flatMapIterable(strings -> strings).log();

        StepVerifier.create(fluxIterable)
                .expectSubscription()
                .expectNext("one", "two", "three", "four")
                .verifyComplete();

        log.info("---------------------------");
        var flatMany = monoList.flatMapMany(Flux::fromIterable).log();
        StepVerifier.create(flatMany)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

    }

    private void doLongAction() {
        log.info("Long Action");
    }
}
