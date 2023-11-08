package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Session extends Observable {

    transient private IMap map;
    private final List<Courier> couriers;

    public Session() {
        this.couriers = new ArrayList<>();
    }

    public IMap getMap() {
        return map;
    }

    public void setMap(IMap map) {
        this.map = map;
        this.notifyObservers(EventType.MAP_LOADED, map);
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public void addCourier(Courier courier) {
        this.couriers.add(courier);
        this.notifyObservers(EventType.COURIER_ADD, courier);
    }

    public void removeCourier(Courier courier) {
        this.couriers.remove(courier);
        this.notifyObservers(EventType.DELIVERY_REMOVE, courier);
    }
}
