package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomSubscriberWithRequest {


    public static void main(String[] args) {
        List<Integer> elements = new ArrayList<>();
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            private volatile Subscription  subscription;
            private int onNextAmount;
            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                this.subscription.request(2);
            }

            @Override
            public void onNext(Integer element) {
                elements.add(element);
                onNextAmount++;
                if(onNextAmount % 2 == 0) {
                    this.subscription.request(2);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.warn("onError: {}", throwable.getMessage());
            }


            @Override
            public void onComplete() {
                log.info("OnCompleted");
            }
        };

        log.info("Elements: {}", elements);

        Flux<Integer> integerFlux = Flux.just(1,2,3,4,5,6,7,8,9,10).log();
        integerFlux.subscribe(subscriber);
    }

}
