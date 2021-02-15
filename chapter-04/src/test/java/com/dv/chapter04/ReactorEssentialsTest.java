package com.dv.chapter04;


import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
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
        try (Connection connection = Connection.newConnection()) {
            connection.getData().forEach(
                    data -> log.info("Received data: {}", data)
            );
        } catch (Exception e) {
            log.info("Error: {}", e.getMessage());
        }
    }

    @Test
    @Ignore
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

    @Test
    @Ignore
    public void testRandom() {
        log.info("Random number is {} /infinity/", random.nextInt());
        log.info("Random number is {} /10/ ", random.nextInt(10));
        log.info("Random number is {} /1000/ ", random.nextInt(1000));
    }

//    @Test
//    @Ignore
//    public void usingWhenExample() throws InterruptedException {
//        Flux.usingWhen(
//                Transaction.beginTransaction(),
//                transaction -> transaction.insertRows(Flux.just("A", "B")),
//                Transaction::commit,
//                Transaction::rollback
//        ).subscribe(
//                d -> log.info("onNext: {}", d),
//                e -> log.info("onError: {}", e.getMessage()),
//                () -> log.info("onComplete")
//        );
//
//        Thread.sleep(1000);
//    }

    @Test
    @Ignore
   public void managingDemand() {
        Flux.range(1, 100)
                .subscribe(
                        data -> log.info("On Next: {}", data),
                        err -> { /* ignore */ },
                        () -> log.info("On Complete"),
                        subscription -> {
                            subscription.request(4);
                            subscription.cancel();
                        }
                );
   }

   @Test
   @Ignore
   public void handlingErrors() throws InterruptedException {
        Flux.just("user-1")
                .flatMap(
                        user -> recommendedBooks(user)
                                .retry(5)
                                .timeout(Duration.ofSeconds(3))
                                .onErrorResume(e -> Flux.just("The Martian"))
                ).subscribe(
                b -> log.info("onNext: {}", b),
                e -> log.warn("onError: {}", e.getMessage()),
                () -> log.info("onComplete !")
        );
        Thread.sleep(5000);
   }

   @Test
   @Ignore
   public void coldPublisher() {
        Flux<String> coldPublisher = Flux.defer(() -> {
            log.info("Generating new items");
            return Flux.just(UUID.randomUUID().toString());
        });
        log.info("No data was generated so far");
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        log.info("Data was generated twice for two subscribers");
   }

   @Test
   @Ignore
   public void exampleRangeFlux() {

        Flux<Integer> range = Flux.range(0, 3)
                .doOnSubscribe(s -> log.info("new subscription"));

        range.subscribe(e -> log.info("[Subscriber 1] onNext: {}", e));
        range.subscribe(e -> log.info("[Subscriber 2] onNext: {}", e));

       log.info("all subscribers are ready");
   }

   @Test
   @Ignore
    public void connectExample() {
        Flux<Integer> source = Flux.range(0, 3)
                .doOnSubscribe(subscription -> log.info("new subscription for the cold publisher"));

       ConnectableFlux<Integer> connectableFlux = source.publish();
       connectableFlux.subscribe(e -> log.info("[Subscriber 1] onNext: {}", e));
       connectableFlux.subscribe(e -> log.info("[Subscriber 2] onNext: {}", e));

       log.info("all subscribers are ready, connecting");
       connectableFlux.connect();
     }

     @Test
     @Ignore
    public void cachingExample() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 2)
                .doOnSubscribe(l -> log.info("new subscription for the cold publisher"));

        Flux<Integer> cachingSource = source.cache(Duration.ofSeconds(1));

        cachingSource.subscribe(e -> log.info("[S 1] onNext: {}", e));
        cachingSource.subscribe(e -> log.info("[S 2] onNext: {}", e));

        Thread.sleep(5000);
        cachingSource.subscribe(e -> log.info("[S 3] onNext: {}", e));
    }

    @Test
    @Ignore
    public void relayExample() throws InterruptedException {

        Flux<Integer> source = Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .doOnSubscribe(l -> log.info("new subscription for the cold publisher"));

        Flux<Integer> cacheSource = source.share();
        cacheSource.subscribe(e -> log.info("[S 1] onNext: {}", e));
        Thread.sleep(400);
        cacheSource.subscribe(e -> log.info("[S 2] onNext: {}", e));
        Thread.sleep(1000);
    }

    @Test
    @Ignore
    public void elapsedExample() throws InterruptedException {
        Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .elapsed()
                .subscribe(e -> log.info("Elapsed {} ms: {}", e.getT1(), e.getT2()));

        Thread.sleep(1000);
    }

    @Test
    @Ignore
    public void transformExample() {
        Function<Flux<String>, Flux<String>> logUserInfo =
                stream -> stream
                        .index()
                        .doOnNext(tp -> log.info("[{}] User: {}", tp.getT1(), tp.getT2()))
                        .map(Tuple2::getT2);

        Flux
                .range(2000, 3)
                .map(i -> "user-" +i)
                .transform(logUserInfo)
                .subscribe(e -> log.info("onNext: {}", e));
    }

    @Test
    @Ignore
    public void composeExample() {
        Function<Flux<String>, Flux<String>> logUserInfo =
                stringFlux -> {
                    if(random.nextBoolean()) {
                        return stringFlux.doOnNext(e -> log.info("[path A}] User {}", e));
                    } else {
                        return stringFlux.doOnNext(e -> log.info("[path B}] User {}", e));
                    }
                };

        // compose doesn't work
        Flux<String> publisher = Flux.just("1", "2").transform(logUserInfo);
        publisher.subscribe();
        publisher.subscribe();
    }


    public Flux<String> recommendedBooks(String userId) {
        return Flux.defer(() -> {
            if (random.nextInt(10) < 7) {
                return Flux.<String>error(new RuntimeException("Conn error"))
                        .delaySequence(Duration.ofMillis(100));
            } else {
                return Flux.just("Blue Mars", "The Expanse")
                        .delayElements(Duration.ofMillis(50));
            }
        }).doOnSubscribe(s -> log.info("Request for {}", userId));
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
            if (random.nextInt(10) < 3) {
                throw new RuntimeException("Communication error");
            }
            return Arrays.asList("Some", "Data");
        }

        @Override
        public void close() {
            log.info("IO Connection closed");
        }
    }

    static class Transaction {
        private static final Random random = new Random();
        private final int id;

        public Transaction(int id) {
            this.id = id;
            log.info("[T: {}] created", id);
        }

        public static Mono<Transaction> beginTransaction() {
            return Mono.defer(() ->
                    Mono.just(new Transaction(random.nextInt(1000))));
        }

        public Flux<String> insertRows(Publisher<String> rows) {
            return Flux.from(rows)
                    .delayElements(Duration.ofMillis(100))
                    .flatMap(row -> {
                        if (random.nextInt(10) < 2) {
                            return Mono.error(new RuntimeException("Error on: " + row));
                        } else {
                            return Mono.just(row);
                        }
                    });
        }


        public Mono<Void> commit() {
            return Mono.defer(() -> {
                log.info("[T: {}] commit", id);
                if (random.nextBoolean()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new RuntimeException("Conflict"));
                }
            });
        }

        public Mono<Void> rollback() {
            return Mono.defer(() -> {
                log.info("[T: {}] rollback", id);
                if (random.nextBoolean()) {
                    return Mono.empty();
                } else {
                    return Mono.error(new RuntimeException("Conn error"));
                }
            });
        }
    }

}
