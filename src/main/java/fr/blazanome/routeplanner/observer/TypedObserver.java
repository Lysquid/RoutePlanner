package fr.blazanome.routeplanner.observer;

/**
 * TypedObserver
 */
public interface TypedObserver<T extends Observable> {

    void update(T observable, EventType eventType, Object message);
    
}
