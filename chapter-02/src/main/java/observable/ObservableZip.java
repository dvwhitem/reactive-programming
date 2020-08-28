package observable;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;

@Slf4j
public class ObservableZip {

    public static void main(String[] args) {

        Observable.zip(
                Observable.just(1, 2, 3, 4),
                Observable.just("A", "B", "C", "D"),
                (x,y) -> y + x
        ).forEach(log::info);
    }
}
