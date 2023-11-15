package fr.blazanome.routeplanner.observer;

/**
 * TypedObserver
 */
public interface TypedObserver<T extends Observable> {

    /**
     * Called each time an observable of type T notify a change
     */
    void update(T observable, EventType eventType, Object message);

}
