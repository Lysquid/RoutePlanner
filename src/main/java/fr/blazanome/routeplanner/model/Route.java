package fr.blazanome.routeplanner.model;

import java.util.List;

/**
 * A route computed for a courier
 * contains the path : a list of consecutive segemnts making the courier route
 * contains the planning : a list of deliveries
 */
public class Route {

    private List<Segment> path;
    private List<Delivery> planning;

    public Route(List<Segment> path, List<Delivery> planning) {
        this.path = path;
        this.planning = planning;
    }

    /**
     * @return the list of consecutive segemnts making the courier route
     */
    public List<Segment> getPath() {
        return path;
    }

    /**
     * @return the list of deliveries in the route
     */
    public List<Delivery> getPlanning() {
        return planning;
    }


}
