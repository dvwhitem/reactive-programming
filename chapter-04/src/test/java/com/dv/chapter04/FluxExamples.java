package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.LinkedList;
import java.util.stream.IntStream;

@Slf4j
public class FluxExamples {

    @Test
    public void fluxSubscribe() {

        var fluxString = Flux.just("A", "B", "C", "D");
        // .concatWith(Flux.error(new RuntimeException("error occurred")));
        fluxString
                .subscribe(
                        (s) -> log.info("Value: {}", s),
                        (err) -> log.info("Error: {}", err),
                        () -> log.info("Complete.")
                );
    }

    @Test
    public void fluxSubscribeLimited() {

        Flux.range(1, 100)
                .subscribe(
                        data -> log.info("Data: {}", data),
                        err -> {
                        },
                        () -> log.info("OnComplete"),
                        subscription -> {
                            subscription.request(7);
                            subscription.cancel();
                        }
                );
    }

    @Test
    public void fluxDisposable() throws InterruptedException {

        var flux = Flux.interval(Duration.ofMillis(10))
                .subscribe((data) -> log.info("Data:  {}", data));

        Thread.sleep(200);
        flux.dispose();
    }

    @Test
    public void fluxBuffer() {
        Flux.range(1, 13)
                .buffer(3)
                .subscribe(integers -> log.info("Elements: {}", integers));
    }

    @Test
    public void windowedByPredicate() {
        Flux<Flux<Integer>> windowedFlux = Flux.range(101, 20)
                .windowUntil(this::isPrime, true);

        windowedFlux
                .subscribe(integerFlux -> integerFlux.collectList()
                        .subscribe(integers -> log.info("window: {}", integers)));

    }

    @Test
    public void fluxGroupBy() {
        Flux.range(1, 7)
                .groupBy(integer -> integer % 2 == 0 ? "Even" : "Odd")
                .subscribe(groupedFlux -> groupedFlux.scan(new LinkedList<>(), (list, element) -> {
                                    list.add(element);
                                    if (list.size() > 3) {
                                        list.remove(0);
                                    }
                                    return list;
                                }).filter(arr -> !arr.isEmpty())
                                .subscribe(objects -> log.info("{}: {}", groupedFlux.key(), objects))
                );
    }

    @Test
    public void fluxGroupByCustom() {
        Flux.range(1, 7)
                .groupBy(integer -> integer % 2 == 0)
                .subscribe(el -> log.info("res: {}", el));
    }

    private boolean isPrime(int number) {
        return number > 2
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }


}
