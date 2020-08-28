package com.dv.chapter_02.rxapp;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Data
public class TemperatureSensor {

    private final Random random = new Random();

    private final Observable<Temperature> dataStream =
            Observable
                    .range(0, Integer.MAX_VALUE)
                    .concatMap(tick -> Observable
                            .just(tick)
                            .delay(random.nextInt(5000), TimeUnit.MILLISECONDS)
                            .map(tickValue -> this.probe()))
                    .publish()
                    .refCount();

    private Temperature probe() {
        return new Temperature(16 + random.nextGaussian() * 10);
    }

}
