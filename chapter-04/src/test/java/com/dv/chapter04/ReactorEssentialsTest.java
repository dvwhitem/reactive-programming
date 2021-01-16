package com.dv.chapter04;


import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.SortedMap;
import java.util.stream.Collector;

@Slf4j
public class ReactorEssentialsTest {

    private final Random random = new Random();

    @Test
    @Ignore
    public void endlessStream() {
        Flux.interval(Duration.ofMillis(1))
                .collectList()
                .block();
    }

    @Test
    @Ignore
    public void endlessStream2() {
        Flux.range(1, 5)
                .repeat()
                .doOnNext(e -> log.info("e {}", e))
                .take(20)
                .blockLast();
    }

    /**
     * Java heap space error
     */
    @Test
    @Ignore
    public void endlessStreamAndCauseAnError() {
        Flux.range(1, 100)
                .repeat()
                .collectList()
                .block();
    }


    @Test
    @Ignore
    public void createFlux() {
        Flux.just("Hello", "World").doOnNext(v -> log.info("value: {}", v)).blockLast();
        Flux.fromArray(new Integer[]{1, 4, 7}).doOnNext(v -> log.info("value: {}", v)).blockLast();
        Flux.fromIterable(Arrays.asList(12, 43, 54)).doOnNext(v -> log.info("value: {}", v)).blockLast();

        Flux<String> emptyStream = Flux.empty();
        Flux<Integer> integerFlux = Flux.range(2010, 11);
        log.info(" {}", integerFlux.collectList().block());
    }

    @Test
    public void createMono() {

        Mono<String> stringMono = Mono.fromCallable(() -> httpsRequest());
        Mono<String> stringMono1 = Mono.fromCallable(this::httpsRequest);

        StepVerifier
                .create(stringMono1)
                .expectErrorMessage("IO error")
                .verify();
    }

    @Test
    @Ignore
    public void shouldCreateDefer() {
        Mono<User> userMono = requestUserData(null);
        StepVerifier.create(userMono)
                .expectNextCount(0)
                .expectErrorMessage("Invalid user id")
                .verify();
    }

    public Mono<User> requestUserData(String userId) {
        return Mono.defer(() ->
                isValid(
                        userId) ?
                        Mono.fromCallable(() -> requestUser(userId)) :
                        Mono.error(new IllegalArgumentException("Invalid user id")));
    }

    @Test
    @Ignore
    public void managingSubscription() throws InterruptedException {
        Disposable disposable = Flux.interval(Duration.ofMillis(50))
                .doOnCancel(() -> log.info("Cancelled")).subscribe(
                        data -> log.info("onNext: {}", data)
                );
        Thread.sleep(300);
        disposable.dispose();
    }

    @Test
    @Ignore
    public void indexElements() {
        Flux.range(2018, 5)
                .timestamp()
                .index()
                .subscribe(e -> log.info("index: {}, ts: {}, value: {} data: {}",
                        e.getT1(),
                        Instant.ofEpochMilli(e.getT2().getT1()),
                        e.getT2().getT2(),
                        e.getT2().getT1()
                        )
                );

    }

    @Test
    @Ignore
    public void startStopStreamProcessing() throws InterruptedException {
        Mono<?> startCommand = Mono.delay(Duration.ofSeconds(2));
        Mono<?> stopCommand = Mono.delay(Duration.ofSeconds(5));

        Flux<Long> streamOfData = Flux.interval(Duration.ofMillis(100));

        streamOfData.subscribe(e -> log.info("Iterator 1  : {}", e));

        streamOfData
                .skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(e -> log.info("Iterator 2 {} ", e));

        Thread.sleep(4000);
    }

    @Test
    @Ignore
    public void sortedCollect() {
        Flux.just(1, 32, 5, 54, 77, 77, 44, 343, 22, 31, 43, 12, 55)
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);

    }

    @Test
    public void defaultIfEmpty() {
        Flux.just()
                .defaultIfEmpty(1)
                .subscribe(e -> log.info(" e: {}", e));
    }

    private User requestUser(String id) {
        return new User();
    }

    private boolean isValid(String userId) {
        return userId != null;
    }

    private String httpsRequest() {
        log.info("Make HTTP request");
        throw new RuntimeException("IO error");
    }

    static class User {
        public String id, name;
    }

}
