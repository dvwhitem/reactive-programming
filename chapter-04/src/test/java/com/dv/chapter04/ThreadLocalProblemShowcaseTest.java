package com.dv.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class ThreadLocalProblemShowcaseTest {

    @Test(expected = NullPointerException.class)
    public void shouldFailDueToDifferentThread() {
        ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();
        threadLocal.set(new HashMap<>());

        Flux.range(0, 10)
                .doOnNext(integer -> threadLocal.get().put(integer, new Random(integer).nextGaussian()))
                .publishOn(Schedulers.parallel())
                .map(i -> threadLocal.get().get(i))
                .blockLast();
    }

}
