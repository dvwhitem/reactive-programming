package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

@Slf4j
public class CustomSubscriber {

    public static void main(String[] args) {
        Subscriber<String> subscriber = new Subscriber<String>() {
            private volatile Subscription  subscription;
            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                log.info("initial request for 1 element");
                subscription.request(1);
            }

            @Override
            public void onNext(String s) {
                log.info("OnNext: {}",s );
                log.info("Requesting 1 more element");
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                log.warn("onError: {}", throwable.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("OnComplete");
            }
        };

        Flux<String> stringFlux= Flux.just("Hello", "Wrorld", "!");
        stringFlux.subscribe(subscriber);
    }
}
