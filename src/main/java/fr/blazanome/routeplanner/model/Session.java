package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Session extends Observable {

    transient private IMap map;
    private List<Courier> couriers;

    public Session() {
        this.couriers = new ArrayList<>();
    }

    public IMap getMap() {
        return map;
    }

    public void setMap(IMap map) {
        this.map = map;
        this.notifyObservers(map);
    }
}
