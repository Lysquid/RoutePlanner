package fr.blazanome.routeplanner.model;

import java.util.ArrayList;
import java.util.List;

import fr.blazanome.routeplanner.graph.Neighbor;

public class IHMTestMap implements IMap {

    private Intersection warehouse;
    private List<Intersection> intersections = new ArrayList<>();
    private List<Segment> segments = new ArrayList<>();
    private List<Segment> exampleRoute = new ArrayList<>();

    public IHMTestMap() {
        Intersection intersection1 = new Intersection(45.75406, 4.857418);
        Intersection intersection2 = new Intersection(45.753277, 4.8788133);
        Intersection intersection3 = new Intersection(45.754982, 4.865772);
        Intersection intersection4 = new Intersection(45.759834, 4.867973);
        Intersection intersection5 = new Intersection(45.749996, 4.858258);
        intersections.add(intersection1);
        intersections.add(intersection2);
        intersections.add(intersection3);
        intersections.add(intersection4);
        intersections.add(intersection5);
        warehouse = new Intersection(45.752, 4.86);
        Segment segment1 = new Segment(intersection1, 2.0, "a", intersection3);
        Segment segment2 = new Segment(intersection1, 4.0, "a", intersection4);
        Segment segment3 = new Segment(intersection2, 6.0, "a", intersection5);
        Segment segment4 = new Segment(intersection2, 7.0, "a", intersection3);
        Segment segment5 = new Segment(intersection3, 8.0, "a", warehouse);
        Segment segment6 = new Segment(intersection4, 9.0, "a", intersection3);
        Segment segment7 = new Segment(intersection4, 10.0, "a", intersection5);
        Segment segment8 = new Segment(intersection5, 8.0, "a", warehouse);
        segments.add(segment1);
        segments.add(segment2);
        segments.add(segment3);
        segments.add(segment4);
        segments.add(segment5);
        segments.add(segment6);
        segments.add(segment7);
        segments.add(segment8);
        exampleRoute.add(segment8);
        exampleRoute.add(segment3);
        exampleRoute.add(segment4);
        exampleRoute.add(segment5);


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

    public Iterable<Segment> getExampleRoute() {
        return this.exampleRoute;
    }

    @Override
    public Intersection getIntersection(int vertexId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getCost(int start, int end) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public Segment getSegment(int start, int end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Neighbor> getNeighbors(int vertex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getVerticesCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getVertexId(Intersection intersection) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVertexId'");
    }

}
