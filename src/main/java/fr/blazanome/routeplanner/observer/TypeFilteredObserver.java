package fr.blazanome.routeplanner.observer;

/**
 * TypeFilteredObserver
 * <p>
 * Wrapper around a {@link TypedObserver} that filters the notification
 * it receives to match the type T
 */
public class TypeFilteredObserver<T extends Observable> implements Observer {

    private Class<T> type;
    private TypedObserver<T> observer;

    public TypeFilteredObserver(Class<T> type, TypedObserver<T> observer) {
        this.type = type;
        this.observer = observer;
    }

    @Override
    public void update(Observable observable, EventType eventType, Object message) {
        if (this.type.isAssignableFrom(observable.getClass())) {
            this.observer.update(type.cast(observable), eventType, message);
        }
    }
}
