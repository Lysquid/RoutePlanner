package fr.blazanome.routeplanner.model;

import java.util.Iterator;

/**
 * Map
 */
public interface IMap extends Graph {

    public Iterator<Segment> getSegments();

    public Segment getSegment(int start, int end);

    public default int getSegmentCount() {
        return this.getVerticesCount();
    }

    public Iterator<Intersection> getIntersections();
    public int getIntersectionCount();

    public Intersection getIntersection(int vertexId);


    public Intersection getWarehouse();
}
