package fr.blazanome.routeplanner.observer;

public interface Observer {
    void update(Observable observable, EventType eventType, Object message);
}
