package fr.blazanome.routeplanner.model;

import fr.blazanome.routeplanner.graph.Graph;

/**
 * Map
 */
public interface IMap extends Graph {

    /**
     * @return an iterator over all segment of the map
     */
    Iterable<Segment> getSegments();

    /**
     * @param start Start vertex number
     * @param end   End vertex number
     * @return the <code>Segment</code> between the two vertices if they are
     *         neighbors and null
     *         otherwise
     */
    Segment getSegment(int start, int end);

    /**
     * @return an iterator over all intersections of the map
     */
    Iterable<Intersection> getIntersections();

    /**
     * @param vertexId Vertex number
     * @return the intersection with <code>vertexId</code> or null if it does not
     *         exist
     */
    Intersection getIntersection(int vertexId);

    /**
     * @param intersection a valid intersection in this map
     * @return the vertexId associeted with this intersection
     */
    int getVertexId(Intersection intersection);

    /**
     * @return The intersection corresponding to the warehouse
     */
    Intersection getWarehouse();
}
