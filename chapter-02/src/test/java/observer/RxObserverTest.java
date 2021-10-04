package observer;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RxObserverTest {

    @Test
    public void managingSubscription() throws InterruptedException {
        CountDownLatch externalSignal = new CountDownLatch(3);

        Subscription subscription = Observable
                .interval(100, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

        externalSignal.await(450, TimeUnit.MILLISECONDS);
        subscription.unsubscribe();
    }

    @Test
    public void exampleZip() {
        Observable.zip(
                Observable.just("A", "B", "C", "D"),
                Observable.just("E", "F", "G", "H"),
                (x, y) -> x+y
        ).forEach(System.out::println);
    }
}
