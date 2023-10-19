package fr.blazanome.routeplanner.model;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private Warehouse warehouse;
    private List<Intersection> intersections;
    private List<Segment> segments;

    public Map() {
        intersections = new ArrayList<>();
        segments = new ArrayList<>();
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }
}
