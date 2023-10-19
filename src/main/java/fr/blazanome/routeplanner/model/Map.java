package fr.blazanome.routeplanner.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Map implements IMap {

    private Intersection warehouse;
    private List<Intersection> intersections;
    private List<Segment> segments;

    public Map() {
        intersections = new ArrayList<>();
        segments = new ArrayList<>();
    }

    public Intersection getWarehouse() {
        return this.warehouse;
    }

    public Iterable<Intersection> getIntersections() {
        return this.intersections;
    }

    public Iterable<Segment> getSegments() {
        return this.segments;
    }

    @Override
    public Intersection getIntersection(int vertexId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getIntersectionCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getCost(int start, int end) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Iterator<Integer> getNeighbors(int vertex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Segment getSegment(int start, int end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getVerticesCount() {
        // TODO Auto-generated method stub
        return 0;
    }

}
