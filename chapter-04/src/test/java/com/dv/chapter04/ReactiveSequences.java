package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Slf4j
public class ReactiveSequences {


    @Test
    public void fluxSequences() {

        Flux.just(32, 1, 45, 3, 5, 7, 12, 43, 22, 101, 4, 4, 34, 54, 23)
                .distinct()
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(m -> log.info("Sorted elements: {}", m));
    }

    @Test
    public void fluxSequencesPredicateAll() {
        Flux.just(12.3,14.2, 19.9)
                .all(m -> m > 10.0)
                .subscribe(e -> log.info("Elements: {}", e));
    }


}
