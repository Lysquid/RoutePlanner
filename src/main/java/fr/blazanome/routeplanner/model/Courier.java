package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.EventType;
import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * A courier, it has an id, a route and a list of delivery requests added to it
 */
public class Courier extends Observable {

    private final int id;
    private Route route;
    private List<DeliveryRequest> requests;

    public Courier(int id) {
        this.id = id;
        this.requests = new ArrayList<>();
        this.route = null;
    }

    /**
     * @return the delivery requests given to the courier
     */
    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    /**
     * @param request the delivery request assigned to the courier
     */
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


    /**
     * @param route the route computed for the delivery requests of the courier
     */
    public void setRoute(Route route) {
        this.route = route;
        this.notifyObservers(EventType.ROUTE_COMPUTED, route);
    }

    /**
     * @param request the delivery request to be removed from the courier
     */
    public void removeDelivery(DeliveryRequest request) {
        this.requests.remove(request);
        this.notifyObservers(EventType.DELIVERY_REMOVE, request);
    }

    /**
     * @return the id (used to identify) of the courier
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Courier " + this.id;
    }
}
