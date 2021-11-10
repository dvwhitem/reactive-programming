package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

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
}
