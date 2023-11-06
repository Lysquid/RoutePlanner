package fr.blazanome.routeplanner.model;

import java.util.List;

/**
 * Route
 */
public class Route {

    private List<Segment> path;
    private List<Delivery> planning;

    public Route(List<Segment> path, List<Delivery> planning) {
        this.path = path;
        this.planning = planning;
    }

    public List<Segment> getPath() {
        return path;
    }

    public List<Delivery> getPlanning() {
        return planning;
    }


}
