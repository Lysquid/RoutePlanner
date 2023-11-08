package fr.blazanome.routeplanner.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    List<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<Observer>();
    }

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    public void notifyObservers(EventType eventType, Object message) {
        for(var observer: this.observers) {
            observer.update(this, eventType, message);
        }
    }
}

