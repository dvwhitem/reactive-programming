package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class FluxReactorTest {

    @Test
    public void endlessStream() {
       Flux<Integer> fluxEndless = Flux.range(1,5).repeat().log();
       fluxEndless.subscribe();
    }

    @Test
    public void endlessStreamOutOfMemory() {
        Flux.range(1, 100)
                .repeat()
                .collectList()
                .block();
    }

    @Test
    public void fluxRange() {
        var fluxRange = Flux.range(2010, 12)
                .take(3)
                .repeat(3)
                .collectList()
                .block();

        log.info("{}", fluxRange);
    }

    @Test
    public void monoCallable() {
        Mono<String> mono = Mono.fromCallable(this::httpRequest).log();
        StepVerifier.create(mono)
                .expectSubscription()
                .expectErrorMessage("IO error")
                .verify();
    }

    @Test
    public void shouldCreateDefer() {
        Mono<User> userMono = requestDeferUserData(null).log();
        StepVerifier.create(userMono)
                .expectSubscription()
                .expectNextCount(0)
                .expectErrorMessage("Invalid user id")
                .verify();
    }

    @Test
    public void shouldCreateUser() {
        Mono<User> userMono = requestUserData(null).log();
        StepVerifier.create(userMono)
                .expectSubscription()
                .expectNextCount(0)
                .expectErrorMessage("Invalid user id")
                .verify();
    }


    public Mono<User> requestDeferUserData(String userId) {
        log.info("call method with defer");
        return Mono.defer(() -> isValid(userId)
                ? Mono.fromCallable(() -> requestUser(userId))
                : Mono.error(new IllegalArgumentException("Invalid user id"))
                );
    }

    public Mono<User> requestUserData(String userId) {
        log.info("call method");
        return isValid(userId)
                ? Mono.fromCallable(() -> requestUser(userId))
                : Mono.error(new IllegalArgumentException("Invalid user id"));
    }


    private String httpRequest() {
        log.info("Making HTTP request");
        throw new RuntimeException("IO error");
    }

    private boolean isValid(String userId) {
        return userId != null;
    }

    private User requestUser(String id) {
        return new User();
    }

    record User(String id, String name) {
        public User() {
            this(null, null);
        }
    };
}
