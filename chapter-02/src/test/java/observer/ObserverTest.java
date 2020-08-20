package observer;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ObserverTest {

    @Test
    public void observersHandleEventsFromSubjectWithAssertions() {

            Subject<String> subject = new ConcreteSubject();
            Observer<String> observerA = Mockito.spy(new ConcreteObserverA());
            Observer<String> observerB = Mockito.spy(new ConcreteObserverB());

            subject.notifyObservers("No listeners");

            subject.registerObserver(observerA);
            subject.notifyObservers("Message for A");

            subject.registerObserver(observerB);
            subject.notifyObservers("Message for A & B");

            subject.unregisterObserver(observerA);
            subject.notifyObservers("Message for B");

            subject.unregisterObserver(observerB);
            subject.notifyObservers("No listeners");

            Mockito.verify(observerA, Mockito.times(1)).observe("Message for A");
            Mockito.verify(observerA, Mockito.times(1)).observe("Message for A & B");
            Mockito.verifyNoMoreInteractions(observerA);

        Mockito.verify(observerB, Mockito.times(1)).observe("Message for B");
        Mockito.verify(observerB, Mockito.times(1)).observe("Message for A & B");
        Mockito.verifyNoMoreInteractions(observerA);

    }
}
