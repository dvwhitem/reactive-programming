import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
public class ReactorExample1 {

    public void output() throws InterruptedException {

        Mono.empty();
        Flux.empty();

        Mono<Integer> integerMono = Mono.just(10);
        Flux<Integer> integerFlux = Flux.just(1, 3, 4, 5, 6, 7);

        Flux<Integer> fluxFromMono = integerMono.flux();
        Mono<Boolean> monoFromFlux = integerFlux.any(s -> s.equals(5));
        Mono<Integer> firstFluxElement = integerFlux.elementAt(1);


        var element = Flux.range(1, 5).blockFirst();
//        log.info("element {}", element);
//
//
//        Flux.range(1, 4)
//         .subscribe(System.out::println);
//
//        Flux.fromIterable(Arrays.asList(1,3,4,6,8))
//                .subscribe(System.out::println);

        Flux.<String>generate(sink -> {
                    sink.next("Hello");
                })
                .delayElements(Duration.ofMillis(500))
                .take(3);


        var telegramProducer = Flux.generate(
                () -> 2354,
                (state, sink) -> {
                    if (state > 2366) {
                        sink.complete();
                    } else {
                        sink.next("Step: " + state);
                    }
                    return state + 3;
                }
        );
        ;

        Flux
                .create(sink -> {
                    telegramProducer.subscribe(new BaseSubscriber<Object>() {
                        @Override
                        protected void hookOnNext(Object value) {
                            sink.next(value);
                        }

                        @Override
                        protected void hookOnComplete() {
                            sink.complete();
                        }
                    });
                    sink.onRequest(r -> {
                        sink.next("DB returns: " + telegramProducer.blockFirst());
                    });
                });

        var second = Flux.just("Word", "Coder").repeat();
        var sumFlux = Flux.from(Flux.just("React", "Reactor", "WebFlux", "NextJs", "JS", "TS"))
                        .zipWith(second, (f, s) -> String.format("%s %s", f,s));

        var  stringFlux =
                sumFlux
                .delayElements(Duration.ofMillis(1300))
                .timeout(Duration.ofSeconds(1))
                //.retry(1)
                //.onErrorReturn("too slow")
                .onErrorResume(throwable -> {
                    //Flux.just("hello", "world"
                    return Flux.interval(Duration.ofMillis(300)).map(String::valueOf);
                })
                .skip(2)
                .take(3);

        stringFlux.subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("finished")

        );

        Thread.sleep(5000);
    }
}
