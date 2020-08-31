package com.dv.chapter_02.rxapp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class RxSseEmitter extends SseEmitter {

    private static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private final Subscriber<Temperature> subscriber;
    private final static AtomicInteger sessionIdSequence = new AtomicInteger(0);
    private final int sessionId = sessionIdSequence.incrementAndGet();

    public RxSseEmitter() {
        super(SSE_SESSION_TIMEOUT);

        this.subscriber = new Subscriber<Temperature>() {
            @Override
            public void onCompleted() {
                log.warn("[{}] Stream completed", sessionId);
            }

            @Override
            public void onError(Throwable e) {
                log.warn("[{}] Received sensor error: {}", sessionId, e.getMessage());
            }

            @Override
            public void onNext(Temperature temperature) {
                try {
                    RxSseEmitter.this.send(temperature);
                    log.info("[{}] << {} ", sessionId, temperature.getValue());
                } catch (IOException e) {
                    log.warn("[{}] Can not send event to SSE, closing subscription, message: {}",
                            sessionId, e.getMessage());
                    unsubscribe();
                }
            }
        };
        onCompletion(() -> {
            log.info("[{}] SSE completed", sessionId);
            subscriber.unsubscribe();
        });
        onTimeout(() -> {
            log.info("[{}] SSE timeout", sessionId);
            subscriber.unsubscribe();
        });
    }
}
