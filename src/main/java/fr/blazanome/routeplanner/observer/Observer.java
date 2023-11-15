package fr.blazanome.routeplanner.observer;

/**
 * Observer
 */
public interface Observer {
    /**
     * Recieve message from an observable
     * @param observable the observable that sent the message
     * @param eventType the type of event
     * @param message the message
     */
    void update(Observable observable, EventType eventType, Object message);
}
