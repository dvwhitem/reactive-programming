package observable;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ObservableInterval {

    public static void main(String[] args) throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(e -> log.info("Received {}", e));

        Thread.sleep(10000);
    }
}
