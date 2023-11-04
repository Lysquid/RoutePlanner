package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Courier extends Observable {

    static int count = 0;

    private List<Segment> currentPath;
    private List<DeliveryRequest> requests;
    private final int id;

    public Courier() {
        this.requests = new ArrayList<>();
        this.currentPath = new ArrayList<>();
        Courier.count += 1;
        this.id = count;
    }

    public List<Segment> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(List<Segment> currentPath) {
        this.currentPath = currentPath;
    }

    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    public void addDelivery(DeliveryRequest request) {
        this.requests.add(request);
        this.notifyObservers(request);
    }

    @Override
    public String toString() {
        return "Courier " + this.id;
    }
}
