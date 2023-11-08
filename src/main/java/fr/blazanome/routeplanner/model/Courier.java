package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Courier extends Observable {

    private final int id;
    private Route route;
    private List<DeliveryRequest> requests;

    public Courier(int id) {
        this.id = id;
        this.requests = new ArrayList<>();
        this.route = null;
    }

    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    public void addDelivery(DeliveryRequest request) {
        this.requests.add(request);
        this.notifyObservers(EventType.DELIVERY_ADD, request);
    }

    /**
     * @return the computed route for the given Courier or null if not alreay computed
     */
    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route route) {
        this.route = route;
        this.notifyObservers(EventType.ROUTE_COMPUTED, route);
    }

    public void removeDelivery(DeliveryRequest request) {
        this.requests.remove(request);
        this.notifyObservers(EventType.DELIVERY_REMOVE, request);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Courier " + this.id;
    }
}
