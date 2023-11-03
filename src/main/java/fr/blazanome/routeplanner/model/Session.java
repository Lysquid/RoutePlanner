package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Session extends Observable {

    transient private IMap map;
    private final List<Courier> couriers;

    public Session() {
        this.couriers = new ArrayList<>();
        this.couriers.add(new Courier());
    }

    public IMap getMap() {
        return map;
    }

    public void setMap(IMap map) {
        this.map = map;
        this.notifyObservers(map);
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public void addCourier(Courier courier) {
        this.couriers.add(courier);
    }

    public void updatedPaths() {
        this.notifyObservers(this);
    }
}
