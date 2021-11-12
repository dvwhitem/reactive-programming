package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Supplier;

@Slf4j
public class MonoExamples {

    private int a = 5;

    @Test
   public void deferExample() {
       Mono<Integer> integerMono = Mono.just(a);
       Mono<Integer> integerMono1 = Mono.defer(() -> Mono.just(a));

       integerMono.subscribe(i -> log.info("{}", i));
       integerMono1.subscribe(i -> log.info("{}", i));

       a = 3;
       integerMono.subscribe(i -> log.info("{}", i));
       integerMono1.subscribe(i -> log.info("{}", i));

   }

   @Test
   public void fluxExample() {

       var fluxRange = Flux
               .range(1, 3)
               .repeat(3)
               .collectList();

        fluxRange.subscribe(i -> log.info("{}",i));
   }

   @Test
   public void monoSupplier() {

        Supplier<String> s = () -> "Hello World!";

        var monoSupplier = Mono.fromSupplier(() -> "Vitaliy, Hi");

        monoSupplier.subscribe((val) -> log.info("val: {}", val));

   }

}
