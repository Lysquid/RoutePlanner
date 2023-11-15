package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the model of the entire application except what is in the state
 */
public class Session extends Observable {

    private IMap map;
    private final List<Courier> couriers;

    /**
     * Creates a new empty session
     */
    public Session() {
        this.couriers = new ArrayList<>();
    }

    /**
     * @return the map in the session
     */
    public IMap getMap() {
        return map;
    }

    /**
     * Sets the map in the session
     *
     * @param map the map
     */
    public void setMap(IMap map) {
        this.map = map;
        this.notifyObservers(EventType.MAP_LOADED, map);
    }

    /**
     * Gets couriers in the session
     * Contract : don't modify directly the list, add or romove the via addCourier or removeCourier
     *
     * @return the list of couriers
     */
    public List<Courier> getCouriers() {
        return couriers;
    }

    /**
     * Given a courier, adds it to the Session
     *
     * @param courier the courier to be added
     */
    public void addCourier(Courier courier) {
        this.couriers.add(courier);
        this.notifyObservers(EventType.COURIER_ADD, courier);
    }

    /**
     * Given a courier, removes it from the Session
     *
     * @param courier the courier to be removed
     */
    public void removeCourier(Courier courier) {
        this.couriers.remove(courier);
        this.notifyObservers(EventType.COURIER_REMOVE, courier);
    }

    /**
     * Replaces the courier list in the session with the couriers in parameters
     *
     * @param couriers couriers to be contained in the session
     */
    public void setCouriers(List<Courier> couriers) {
        for (Courier courier : this.couriers) {
            this.notifyObservers(EventType.COURIER_REMOVE, courier);
        }

        this.couriers.clear();
        for (Courier courier : couriers) {
            this.addCourier(courier);
        }
    }
}
