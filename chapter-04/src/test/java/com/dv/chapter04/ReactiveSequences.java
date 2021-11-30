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

    @Test
    public void fluxSequencesPredicateAny() {
        Flux.just("Spring", "Spring Cloud", "Spring Boot", "WebFlux")
                .any(m -> m.equalsIgnoreCase("spring"))
                .subscribe(e -> log.info("Any element {}", e));
    }

    @Test
    public void fluxRangeAndReduce() {
            Flux
                    .range(1, 5)
                    .reduce(0, (e,a) -> e + a)
                    .subscribe(e -> log.info("Element e {}", e));
    }

    @Test
    public void fluxScan() {
        Flux.range(1,5)
                .scan(0, (a, e) -> a + e)
                .subscribe(m -> log.info("Element: {}", m));
    }

    @Test
    public void fluxThenMany() {
        Flux.just(1,2,3,4,5,6,7,8)
                .skipUntil(el -> el == 7)
                .thenMany(Flux.range(1,6))
                .subscribe(m -> log.info("Element {}", m));
    }


}
