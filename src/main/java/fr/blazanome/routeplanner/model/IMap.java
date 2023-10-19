package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.graph.Graph;

/**
 * Map
 */
public interface IMap extends Graph {

    /**
     * @return an iterator over all segment of the map
     */
    public Iterable<Segment> getSegments();

    /**
     * @param start Start vertex number
     * @param end   End vertex number
     * @return the <code>Segment</code> between the two vertices if they are
     *         neighbors and null
     *         otherwise
     */
    public Segment getSegment(int start, int end);

    /**
     * @return the total number of segments
     */
    public default int getSegmentCount() {
        return this.getVerticesCount();
    }

    /**
     * @return an iterator over all intersections of the map
     */
    public Iterable<Intersection> getIntersections();

    /**
     * @return the total number of intersections
     */
    public int getIntersectionCount();

    /**
     * @param vertexId Vertex number
     * @return the intersection with <code>vertexId</code> or null if it does not
     *         exist
     */
    public Intersection getIntersection(int vertexId);

    /**
     * @return The intersection corresponding to the warehouse
     */
    public Intersection getWarehouse();
}
