package fr.blazanome.routeplanner.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Extend this class to become an observable that can send messages to its observers
 */
public class Observable {
    List<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<Observer>();
    }

    /**
     * Register on observer
     *
     * @param o the observer
     */
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    /**
     * Removes the observer
     *
     * @param o the observer
     */
    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    /**
     * Sends a notification to all the observers
     *
     * @param eventType the type of event
     * @param message   a message to pass with the event
     */
    public void notifyObservers(EventType eventType, Object message) {
        for (var observer : this.observers) {
            observer.update(this, eventType, message);
        }
    }
}

