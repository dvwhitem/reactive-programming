package com.dv.chapter04;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxOperators {

    @Test
    public void fluxConcatTest() {
        var fluxInteger = Flux.concat(
                Flux.range(1, 3),
                Flux.just(4,5)
        );

        StepVerifier
                .create(fluxInteger.log())
                .expectSubscription()
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

    @Test
    public void fluxMerge() {
        var flux1 = Flux.just("Spring", "Spring Boot");
        var flux2 = Flux.just("Webflux", "Spring Cloud");

        var mergeString = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeString.log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

    }
}
