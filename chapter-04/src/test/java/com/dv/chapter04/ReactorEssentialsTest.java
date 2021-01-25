package com.dv.chapter04;


import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

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
    @Ignore
    public void defaultIfEmpty() {
        Flux.just()
                .defaultIfEmpty(1)
                .subscribe(e -> log.info(" e: {}", e));
    }

    @Test
    public void hasAnyElement() {
        Flux.just(33, 3, 53, 5, 73, 87, 71, 95, 89, 1, 91)
                .any(e -> e % 2 == 0)
                .subscribe(hasEvens -> log.info("Has evens {}", hasEvens));
    }

    @Test
    @Ignore
    public void reduceElements() {
        Flux.range(1, 5)
                .reduce(0, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));
    }

    @Test
    @Ignore
    public void scanElements() {
        Flux.range(1, 5)
                .scan(1, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));
    }

    @Test
    @Ignore
    public void thenManyFlux() {
        Flux.just(1, 2, 3)
                .thenMany(Flux.just(4, 5, 6))
                .subscribe(e -> log.info("element: {}", e));
    }

    @Test
    @Ignore
    public void concatFlux() {
        Flux.concat(
                Flux.range(1, 4),
                Flux.just(8, 9, 10)
        )
                .subscribe(e -> log.info("elements: {}", e));
    }

    @Test
    @Ignore
    public void bufferedFlux() {
        Flux.range(1, 17)
                .buffer(4)
                .subscribe(e -> log.info("elem: {}", e));
    }

    @Test
    @Ignore
    public void windowedFlux() {
        Flux<Flux<Integer>> windowedFlux = Flux.range(101, 20)
                .windowUntil(this::isPrime, true);

        windowedFlux.subscribe(window -> window.collectList().subscribe(e -> log.info("window {}", e)));
    }

    @Test
    public void groupByExample() {
        Flux.range(1, 101)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd")
                .subscribe(groupFlux -> groupFlux.scan(
                        new LinkedList<>(),
                        (list, elem) -> {
                            if (list.size() > 2) {
                                list.remove(0);
                            }
                            list.add(elem);
                            return list;
                        }
                )
                        .filter(arr -> !arr.isEmpty())
                        .subscribe(data -> log.info("{}: {}", groupFlux.key(), data)));
    }

    @Test
    @Ignore
    public void flatMapExample() throws InterruptedException {
        Flux
                .just("user-1", "user-2", "user-3")
                .flatMap(u -> requestBooks(u)
                        .map(b -> u + "/" + b))
                .subscribe(r -> log.info("onNext: {}", r));
        Thread.sleep(1500);
    }

    @Test
    @Ignore
    public void sampleExample() throws InterruptedException {
        Flux.range(1, 100)
                .delayElements(Duration.ofMillis(1))
                .sample(Duration.ofMillis(20))
                .subscribe(e -> log.info("e: {}", e));

        Thread.sleep(1000);
    }

    @Test
    @Ignore
    public void doOnNextExample() {
        Flux.just(1, 2, 3)
                .concatWith(Flux.error(new RuntimeException("Conn error")))
                .doOnEach(s -> log.info("signal: {}", s))
                .subscribe();
    }

    @Test
    @Ignore
    public void signalProcessing() {
        Flux.range(1, 3)
                .doOnNext(e -> log.info("data: {}", e))
                .materialize()
                .doOnNext(e -> log.info("signal: {}", e))
                .dematerialize()
                .collectList()
                .subscribe(e -> log.info("result: {}", e));
    }

    @Test
    @Ignore
    public void usingPushOperator() {
        Flux
                .push(emitter -> IntStream.range(2000, 200000).forEach(emitter::next))
                .delayElements(Duration.ofMillis(3))
                .subscribe(el -> log.info("onNext: {}", el));
    }

    @Test
    @Ignore
    public void usingCreateOperator() throws InterruptedException {
        Flux.create(emitter -> {
            emitter.onDispose(() -> log.info("Disposed"));
            // push events to emitter
        })
                .subscribe(e -> log.info("onNext: {}", e));

        Thread.sleep(1000);
    }


    @Test
    @Ignore
    public void usingGenerate() throws InterruptedException {
        Flux.generate(
                () -> Tuples.of(0L, 1L),
                (state, sink) -> {
                    log.info("generated value: {}", state.getT2());
                    sink.next(state.getT2());
                    long newValue = state.getT1() + state.getT2();
                    return Tuples.of(state.getT2(), newValue);
                })
                .take(7)
                .subscribe(e -> log.info("onNext: {}", e));

        Thread.sleep(100);
    }

    @Test
    @Ignore
    public void tryWithResource() {
        try(Connection connection = Connection.newConnection()) {
            connection.getData().forEach(
                    data -> log.info("Received data: {}", data)
            );
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
    }

    @Test
    public void usingOperator() {
        Flux<String> ioRequestResult = Flux.using(
                Connection::newConnection,
                connection -> Flux.fromIterable(connection.getData()),
                Connection::close
        );

        ioRequestResult.subscribe(
                data -> log.info("ReceivedData data: {}", data),
                e -> log.info("Error Message: {}", e.getMessage()),
                () -> log.info("Stream finished")
        );
    }

    private Flux<String> requestBooks(String user) {
        return Flux.range(1, random.nextInt(3) + 1)
                .map(i -> "book-" + i)
                .delayElements(Duration.ofMillis(5));
    }

    private boolean isPrime(int number) {
        return number > 2
                && IntStream
                .rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n) == 0);
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

    static class Connection implements AutoCloseable {

        private final Random random = new Random();

        static Connection newConnection() {
            log.info("IO Connection created");
            return new Connection();
        }

        public Iterable<String> getData() {
            if(random.nextInt(10) < 3) {
                throw new RuntimeException("Communication error");
            }
            return Arrays.asList("Some", "Data");
        }

        @Override
        public void close() {
            log.info("IO Connection closed");
        }
    }

}
