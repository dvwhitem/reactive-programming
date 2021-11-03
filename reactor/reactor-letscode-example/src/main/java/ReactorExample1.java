import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
public class ReactorExample1 {

    public void output() {

        Mono.empty();
        Flux.empty();

        Mono<Integer> integerMono =  Mono.just(10);
        Flux<Integer> integerFlux = Flux.just(1,3,4,5,6,7);

        Flux<Integer> fluxFromMono = integerMono.flux();
        Mono<Boolean> monoFromFlux = integerFlux.any(s -> s.equals(5));
        Mono<Integer> firstFluxElement = integerFlux.elementAt(1);


        var element =  Flux.range(1, 5).blockFirst();
        //log.info("element {}", element);


        //Flux.range(1, 4)
        // .subscribe(System.out::println);

        Flux.fromIterable(Arrays.asList(1,3,4,6,8))
                .subscribe(System.out::println);
    }
}
