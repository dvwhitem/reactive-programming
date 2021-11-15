package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

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
                        err -> {},
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


}
