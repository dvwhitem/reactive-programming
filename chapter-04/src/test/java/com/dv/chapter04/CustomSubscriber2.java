package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class CustomSubscriber2 {

    @Test
    public void testMySubscriber() {
        Flux.range(1, 10)
                .subscribe(new MySubscriber<>());
    }

    public static class MySubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
            log.info("Subscribed");
            request(2);
        }

        public void hookOnNext(T value) {
           log.info("OnNext: {}", value);
               if((Integer) value > 1) {
                   log.info("More {} request", value);
                   request(5);
               }
        }

    }
}
