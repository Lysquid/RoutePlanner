package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Courier extends Observable {

    static int count = 0;

    private Route route;
    private List<DeliveryRequest> requests;
    private final int id;

    public Courier() {
        this.requests = new ArrayList<>();
        this.route = null;
        Courier.count += 1;
        this.id = count;
    }

    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    public void addDelivery(DeliveryRequest request) {
        this.requests.add(request);
        this.notifyObservers(request);
    }

    /**
     * @return the computed route for the given Courier or null if not alreay computed
     */
    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route route) {
        this.route = route;
        this.notifyObservers(route);
    }


    @Override
    public String toString() {
        return "Courier " + this.id;
    }
}
