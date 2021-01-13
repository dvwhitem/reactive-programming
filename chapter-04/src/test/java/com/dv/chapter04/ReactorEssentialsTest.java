package com.dv.chapter04;



import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

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
        Flux.range(1,100)
                .repeat()
                .collectList()
                .block();
    }

}
