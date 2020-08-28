package observable;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;

@Slf4j
public class Main {
    public static void main(String[] args) {

        Observable.create(subscriber ->{
                    subscriber.onNext("Hello, reactive world!");
                    subscriber.onCompleted();
                }).subscribe(System.out::println, System.err::println, () -> log.info("Done!"));

    }


}
