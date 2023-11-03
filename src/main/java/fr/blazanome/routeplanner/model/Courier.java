package fr.blazanome.routeplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Courier {

    private List<Segment> currentPath;
    private List<Integer> deliveries;

    public Courier() {
        this.deliveries = new ArrayList<>();
        this.currentPath = new ArrayList<>();
    }

    public List<Segment> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(List<Segment> currentPath) {
        this.currentPath = currentPath;

    }

    public List<Integer> getDeliveries() {
        return deliveries;
    }

    public void addDelivery(Integer delivery) {
        this.deliveries.add(delivery);
    }
}
